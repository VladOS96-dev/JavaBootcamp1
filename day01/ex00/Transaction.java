package ex00;
import java.util.UUID;
public class Transaction {
    private  UUID identifier;
    private  User recipient;
    private  User sender;
    private  TransferCategory transferCategory;
    private  double transferAmount;
    public  Transaction(User recipient,User sender,TransferCategory transferCategory,double transferAmount)
    {
        if(recipient==null||sender==null)
        {
            throw new NullPointerException();
        }
        this.identifier=UUID.randomUUID();
        this.recipient=recipient;
        this.sender=sender;
        this.transferCategory=transferCategory;
        this.transferAmount=transferAmount;
    }
    public double getTransferAmount() {
        return transferAmount;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public User getRecipient() {
        return recipient;
    }

    public User getSender() {
        return sender;
    }

    public TransferCategory getTransferCategory() {
        return transferCategory;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setTransferAmount(double transferAmount) {
        if (transferCategory == TransferCategory.CREDIT && (transferAmount > 0 || sender.getBalance() < transferAmount))
        {
            this.transferAmount=0;
        }
        else if(transferCategory==TransferCategory.DEBIT&&(transferAmount<0||sender.getBalance()>transferAmount))
        {
            this.transferAmount=0;
        }
        else {
            this.transferAmount = transferAmount;
            if(transferCategory==TransferCategory.DEBIT)
            {
                sender.setBalance(sender.getBalance()-transferAmount);
                recipient.setBalance(recipient.getBalance()+transferAmount);
            }
            else if(transferCategory==TransferCategory.CREDIT)
            {
                sender.setBalance(sender.getBalance()+transferAmount);
                recipient.setBalance(recipient.getBalance()-transferAmount);
            }
        }

    }

    public void setTransferCategory(TransferCategory transferCategory) {
        if (this.transferCategory != transferCategory)
        {
            transferAmount=-transferAmount;
        }
        this.transferCategory = transferCategory;
    }
    public void printInfo()
    {
        System.out.println("Identificator: "+identifier);
        System.out.println("Name recipient: "+recipient.getUserName());
        System.out.println("Name sender: "+sender.getUserName());
        System.out.println("Transfer category: "+transferCategory.toString());
        System.out.println("Transfer amount: "+transferAmount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "Id=" + identifier +
                ", Recipient=" + recipient +
                ", Sender=" + sender +
                ", TransferCategory=" + transferCategory +
                ", TransferAmount=" + transferAmount +
                '}';
    }
}
