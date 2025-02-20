package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import edu.school21.sockets.services.RoomsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0 || !args[0].startsWith("--port=")) {
            System.out.println("Error: Port is not specified. Use --port=PORT_NUMBER");
            return;
        }
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
            int port = Integer.parseInt(args[0].split("=")[1]);


            RoomsService roomsService = context.getBean(edu.school21.sockets.services.RoomsService.class);

            Server server = new Server(port, context.getBean(edu.school21.sockets.services.UsersService.class), roomsService);
            server.start();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}