## API

### Find all persons
GET .../persons
Response
```json
{
  "person_list": [
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
    ]
}
```

### Add person
POST .../persons
Request
```json
{
  "person_data": {
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```

Response
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```
### Add/DELETE number
POST .../numbers
Request
```json
{
  "number": "String",
  "person_id": "UUID"
}
```

Response    
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```

DELETE .../numbers?number="String"

Response
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "deleted_number" : "String"
  }
}
```

### Find|Delete|Update Person

GET .../persons?id="UUID"

Response
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```

GET .../persons?last_name="String"&number="String"

Response
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```

DELETE .../persons?id="UUID"

Response
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```

PUT .../persons

Request
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```

Response
```json
{
  "person_data": {
    "id": "UUID",
    "first_name":"String",
    "last_name": "String",
    "numbers": {
      "number1" : "String",
      "number2" : "String",
      "number3" : "String"
    }
  }
}
```
### Возможные ошибки
#### При вставке
* Пользователь с такими параметрами уже есть
* В базе не нашел фамилию/номер телефона
* Не смогли обновить/удалить данные