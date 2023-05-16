# CarNotify
This project created for online car services.

to Run project


- mvn clean compile assembly:single  
- docker build -t carnotify .  
- docker run -e TELEGRAM_BOT_TOKEN={{telegramToken}} -e TELEGRAM_CHAT_ID={{telegramChatId}} -p 8080:8080 -t carnotify
