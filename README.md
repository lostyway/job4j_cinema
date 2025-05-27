# job4j_cinema

## Описание проекта
Данный проект представляет собой веб-приложение для записи на предстоящие киносеансы (на ближайшую неделю), а также для просмотра кинотеки, которая сейчас активна.  
Цель — получить практический опыт разработки с использованием современных технологий, а также реализовать основные функциональные требования по курсу.  
Проект построен с упором на читаемость кода, масштабируемость и использование best practices.

## Стек технологий
- Java 17  
- Maven 3.8.6  
- PostgreSQL 15  
- Spring Boot 3.4.0  
- Liquibase 4.29  
- Thymeleaf  
- Sql2o 1.6.0  
- H2 2.3.232  
- JaCoCo 0.8.12  

## Требования к окружению
- JDK 17 (установленный и настроенный PATH)  
- Maven 3.8+  
- PostgreSQL 15

## Запуск проекта

1. Клонировать репозиторий  
```bash
git clone https://github.com/your_username/job4j_cinema.git
cd job4j_cinema
```

2. Создать базу данных в PostgreSQL
```sql
create database cinemadb;
```

3. Применить миграции Liquibase (если есть)
```
mvn liquibase:update
```

4. Собрать и запустить проект
```
mvn clean install
mvn spring-boot:run
```

5. Открыть в браузере

http://localhost:8080

## Взаимодействие с приложением

        Страница входа/регистрации:
        
![изображение](https://github.com/user-attachments/assets/29db56e6-ac70-4469-9f4a-ea2802e8898d)
![изображение](https://github.com/user-attachments/assets/fc938c11-527d-4e49-849a-6ebb52842953)


        Главная страница:
        
![изображение](https://github.com/user-attachments/assets/7911deea-1628-4a43-9b7b-8d567cceb385)

        Кинотека:
![изображение](https://github.com/user-attachments/assets/46d01f17-2fee-4f66-b3b1-9bab6c1154db)
        
        Расписание сеансов:
![изображение](https://github.com/user-attachments/assets/9b8c3c70-3e44-43e8-883e-e274a0afed67)
![изображение](https://github.com/user-attachments/assets/87027be0-f492-4d26-b0e9-cbb34399e1e1)

        Покупка билета:
![изображение](https://github.com/user-attachments/assets/8b724693-665a-463e-a373-46d1ea835777)
        Сам билет:
![изображение](https://github.com/user-attachments/assets/6b518521-9ee3-4034-ae0d-18731427e85a)



## Контакты

    Email: hardsheller@gmail.com

    Telegram: @lostyway

Буду рад ответить на вопросы и получить обратную связь!
