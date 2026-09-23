# Платёжный шлюз

Учебный Java-проект команды Охотникова Святослава, Сторожева Романа и
Паламарчука Даниила. Текущий этап: **практика 3 — PostgreSQL, JDBC и CRUD**.

## Что нужно для запуска

- JDK 17 или новее;
- PostgreSQL на `localhost:5432`;
- настроенная локальная база `payment_gateway` в кодировке UTF-8;
- утилита `curl` для загрузки JDBC-драйвера.

Проект использует официальный JDBC-драйвер PostgreSQL `42.7.13`. Команда
`make compile` при первом запуске скачивает один JAR-файл в `build/lib`.
Пароль базы данных в исходниках не хранится.

## Запуск

```sh
DB_PASSWORD='ваш_пароль' make run
```

По умолчанию используются:

```text
DB_URL=jdbc:postgresql://localhost:5432/payment_gateway
DB_USER=postgres
```

При необходимости их можно переопределить:

```sh
DB_URL='jdbc:postgresql://localhost:5432/payment_gateway' \
DB_USER='postgres' \
DB_PASSWORD='ваш_пароль' \
make run
```

## Возможности

| Пункт | Действие | Операция с БД |
| --- | --- | --- |
| `1` | Показать все платежи | `SELECT` |
| `2` | Показать учебный магазин | `SELECT` при запуске |
| `3` | Изменить описание платежа | `UPDATE` |
| `4` | Добавить платёж | `INSERT` |
| `5` | Найти платёж по ID | `SELECT ... WHERE` |
| `6` | Фильтр по минимальной сумме | `SELECT ... WHERE` |
| `7` | Сортировка по сумме | `SELECT ... ORDER BY` |
| `8` | Удалить платёж | `DELETE` |
| `9` | Создать магазин с первым платежом | транзакция из двух `INSERT` |
| `0` | Выйти | — |

Изменения теперь сохраняются после завершения приложения.

## Схема

```text
merchants
    id          BIGINT PRIMARY KEY
    name        VARCHAR(100)
    website     VARCHAR(255)

payments
    id          BIGINT PRIMARY KEY
    amount      NUMERIC(12, 2)
    currency    CHAR(3)
    description TEXT
    merchant_id BIGINT REFERENCES merchants(id)
```

Один магазин может иметь несколько платежей. Значение `payments.merchant_id`
должно ссылаться на существующую строку `merchants.id`.

Таблицы уже созданы в локальной базе через pgAdmin. Отдельного SQL-файла для
повторного создания схемы в проекте нет.

## Структура Java-кода

```text
src/main/java/ru/university/paymentgateway/
    Main.java
    console/
        ConsoleMenu.java
    dao/
        MerchantDao.java
        PaymentDao.java
    database/
        DatabaseConfig.java
        DatabaseException.java
    model/
        Merchant.java
        Payment.java
        InvalidPaymentException.java
    service/
        PaymentService.java
        RegistrationService.java
```

`ConsoleMenu` работает только с сервисами и не содержит SQL. `PaymentService`
проверяет простые бизнес-правила. DAO выполняют запросы через
`PreparedStatement`, а соединения, запросы и результаты закрываются с помощью
try-with-resources.

`RegistrationService` создаёт магазин и первый платёж через одно соединение.
Сначала отключается автоматический commit, затем выполняются два `INSERT`.
При успехе вызывается `commit()`, при ошибке — `rollback()`.

## Проверка для защиты

1. Пунктом `1` покажите три исходных платежа.
2. Пунктом `4` добавьте платёж и перезапустите программу: запись сохранится.
3. Пунктом `3` измените его описание, а пунктом `5` найдите результат.
4. Пунктами `6` и `7` покажите фильтрацию и сортировку средствами SQL.
5. Пунктом `8` удалите добавленный платёж.
6. Пунктом `9` создайте новый магазин и его первый платёж.
7. Повторите пункт `9`, указав свободный ID магазина, но занятый ID платежа
   `1002`. Платёж не добавится, а магазин тоже не сохранится благодаря rollback.

Реальных переводов денег программа не выполняет. Это учебная модель хранения
магазинов и платежей.

Подробные объяснения находятся в Obsidian, в разделе
`Программирование корпоративных систем/Разборы практик`.
