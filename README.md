# CarNotify
This project created for online car services.

to Run project


- mvn clean compile assembly:single  
- docker build -t carnotify .  
- docker run -e TELEGRAM_BOT_TOKEN=bot5964009927:AAGvqBmXB9UdAhDK0QM6DrJvt090YeH1S3Q -e TELEGRAM_CHAT_ID=-1001855615886 -p 8080:8080 -t carnotify

