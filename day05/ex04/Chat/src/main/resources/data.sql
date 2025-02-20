insert into chat.user(login,password) values('admin','123456'),
('worker1','asd'),
('worker2','vervdv'),
('worker3','12ddas'),
('worker4','1ewfew')
ON CONFLICT DO NOTHING;

insert into chat.chatroom(room_name,owner) values
('room1',(select id from chat.user where login='admin')),
('room2',(select id from chat.user where login='worker4')),
('room3',(select id from chat.user where login='worker1')),
('room4',(select id from chat.user where login='worker3')),
('room5',(select id from chat.user where login='worker2'))
ON CONFLICT DO NOTHING;

insert into chat.message(author,room,text) values
((select id from chat.user where login='admin'),(select id from chat.chatroom where room_name='room1'),'hello'),
((select id from chat.user where login='worker1'),(select id from chat.chatroom where room_name='room2'),'test'),
((select id from chat.user where login='worker2'),(select id from chat.chatroom where room_name='room3'),'works'),
((select id from chat.user where login='worker3'),(select id from chat.chatroom where room_name='room4'),'world'),
((select id from chat.user where login='worker4'),(select id from chat.chatroom where room_name='room5'),'java')
ON CONFLICT DO NOTHING;
INSERT INTO chat.user_chatroom (user_id, chat_id) values
((SELECT id FROM chat.user WHERE login = 'admin'), (SELECT id FROM chat.chatroom WHERE room_name = 'room1')),
((SELECT id FROM chat.user WHERE login = 'worker1'), (SELECT id FROM chat.chatroom WHERE room_name = 'room2')),
((SELECT id FROM chat.user WHERE login = 'worker2'), (SELECT id FROM chat.chatroom WHERE room_name = 'room3')),
((SELECT id FROM chat.user WHERE login = 'worker3'), (SELECT id FROM chat.chatroom WHERE room_name = 'room4')),
((SELECT id FROM chat.user WHERE login = 'worker4'), (SELECT id FROM chat.chatroom WHERE room_name = 'room5'));