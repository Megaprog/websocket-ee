# Java EE websocket implementation

Using WebSocket in Java EE environment. Tested on WildFly 10.0.0.Final

## How to package it?

    mvn clean package

websocket-ee.war file will be created at target directory

## Protocol description

Output message:
```json
{ 
  "type": "TYPE_OF_MESSAGE" , // строка, тип сообщения
  "sequence_id": "09caaa73-b2b1-187e-2b24-683550a49b23", // строка, идентификатор связанности сообщений
  "data" : {} // объект, содержит данные запроса
}
```

Input message:
```json
{ 
  "type": "TYPE_OF_MESSAGE" , // строка, тип сообщения
  "sequence_id": "09caaa73-b2b1-187e-2b24-683550a49b23", // строка, идентификатор связанности сообщений
  "data" : {} // объект, содержит данные ответа
}
```

В случае возврата сообщения об ошибке, объект data должен содержать следующие поля:
"error_description":"Customer not found", // поле с описанием ошибки
"error_code":"customer.notFound" // поле с кодом ошибки

Запрос для успешной операции аутентификации:
```json
{
  "type":"LOGIN_CUSTOMER",
  "sequence_id":"a29e4fd0-581d-e06b-c837-4f5f4be7dd18",
  "data":{
    "email":"fpi@bk.ru",
    "password":"123123"
  }
}
```

Ответ для успешной операции аутентификации: 
```json
{
  "type":"CUSTOMER_API_TOKEN",
  "sequence_id":"cbf187c9-8679-0359-eb3d-c3211ee51a15",
  "data":{
    "api_token":"afdd312c-3d2a-45ee-aa61-468aba3397f3",
    "api_token_expiration_date":"2015-07-15T11:14:30Z"
  }
}
```

Запрос для не успешной операции аутентификации: 
```json
{
  "type":"LOGIN_CUSTOMER",
  "sequence_id":"715c13b3-881a-9c97-b853-10be585a9747",
  "data":{
    "email":"123@gmail.com",
    "password":"newPassword"
  }
}
```

Ответ для не успешной операции аутентификации: 
```json
{
  "type":"CUSTOMER_ERROR",
  "sequence_id":"715c13b3-881a-9c97-b853-10be585a9747",
  "data":{
    "error_description":"Customer not found",
    "error_code":"customer.notFound"
  }
}
```
