# Дипломный проект по курсу «Тестировщик ПО»
## О проекте

Автоматизированное тестирование комплексного сервиса по покупке туров, взаимодействующего с СУБД и API Банка. Купить тур можно двумя способами:
- оплатив тур с помощью дебетовой карты;
- покупки тура в кредит.

## Документация

- [План автоматизации тестирования](https://github.com/ElenaLilu/QA_Diploma_Imyarekova/blob/main/documents/Plan.md)
- [Отчет по итогам тестирования](https://github.com/ElenaLilu/QA_Diploma_Imyarekova/blob/main/documents/Report.md)
- [Комплексный отчет по итогам всего процесса автоматизации](https://github.com/ElenaLilu/QA_Diploma_Imyarekova/blob/main/documents/Summary.md)

## Алгоритм для запуска автотестов
1. Клонировать проект: `https://github.com/ElenaLilu/QA_Diploma_Imyarekova`
2. Открыть проект в IntelliJ IDEA
3. Запустить Docker Desktop командой в терминале IntelliJ IDEA `docker-compose up`

### Подключение SUT к MySQL
1. В терминале 3 запустить приложение: ` java -jar artifacts/aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app `
2. Проверить запуск приложение в браузере Chrome:`http://localhost:8080`
3. В терминале 4 запустить тесты: `./gradlew clean test "-Ddatasource.url=jdbc:mysql://localhost:3306/mysql"` 
4. Создать отчёт Allure и открыть в браузере `.\gradlew allureServe`
5. Закрыть отчёт в терминале 4: `CTRL + C` -> `y` -> `Enter`
6. Остановить приложение в терминале 3: `CTRL + C`
7. Остановить контейнеры в терминале 1:`docker-compose down`

### Подключение SUT к PostgreSQL
1. В терминале 3 запустить приложение: `java -jar artifacts/aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`
2. Проверить запуск приложение в браузере Chrome:http://localhost:8080
3. В терминале 4 запустить тесты: `./gradlew clean test "-Ddatasource.url=jdbc:postgresql://localhost:5432/postgresql"`
4. Создать отчёт Allure и открыть в браузере `.\gradlew allureServe`
5. Закрыть отчёт в терминале 4: `CTRL + C` -> `y` -> `Enter`
6. Остановить приложение в терминале 3: `CTRL + C`
7. Остановить контейнеры в терминале 1:`docker-compose down`
