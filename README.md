# Описание задания

## Задача

Реализовать программу для эмуляции работы коммутатора, генерирующую CDR файлы, а также сервис для агрегации этих данных в UDR отчеты.

## Инструменты

- OpenJDK 17
- Maven/Gradle
- Junit5

## Дано

- Звонки абонентов сотового оператора фиксируются в CDR файлы на коммутаторах.
- Для стандартизации данных между операторами используется стандарт BCE.
- CDR файлы содержат следующие данные:
  - Тип вызова (01 - исходящие, 02 - входящие)
  - Номер абонента
  - Время начала звонка (Unix time)
  - Время окончания звонка
- UDR отчеты агрегируют данные по абонентам и суммируют длительность вызовов разного типа.

Вот пример фрагмента CDR:

```
02,79876543221, 1709798657, 1709799601
01,79996667755, 1709899870, 1709905806
```

UDR будет агрегировать данные по абонентам и суммировать длительность вызовов разного типа. Пример UDR объекта для абонента 79876543221:

```json
{
    "msisdn": "79876543221",
    "incomingCall": {
        "totalTime": "02:12:13"
    },
    "outcomingCall": {
        "totalTime": "00:02:50"
    }
}
```

## Задача 1

Написать сервис для эмуляции работы коммутатора, генерирующий CDR файлы.

### Условия

1. 1 CDR файл = 1 месяц. Тарифицируемый период - 1 год.
2. Данные в CDR файлах не упорядочены.
3. Количество и длительность звонков определяется случайным образом.
4. Список абонентов (не менее 10) хранится в локальной БД (H2).
5. После генерации CDR, данные о транзакциях пользователя помещаются в соседнюю таблицу этой БД.

## Задача 2

Передать данные, полученные от сервиса CDR, в сервис генерации UDR. Агрегировать данные по каждому абоненту в отчет.

### Условия

1. Данные берутся только из CDR файлов. Доступа к БД с описанием транзакций нет.
2. Сгенерированные отчеты UDR сохраняются в /reports.
3. Шаблон имени файла: номер_месяц.json (например, 79876543221_1.json).
4. Класс генератора отчетов содержит методы:
   - generateReport(): сохраняет все отчеты и выводит в консоль таблицу с абонентами и итоговым временем звонков.
   - generateReport(msisdn): сохраняет все отчеты и выводит в консоль таблицу для одного абонента и его итогового времени звонков за каждый месяц.
   - generateReport(msisdn, month): сохраняет отчет и выводит в консоль таблицу для одного абонента и его итогового времени звонков за указанный месяц.

## Общие условия

1. Решение должно быть описано в одном модуле.
2. Допустимо использовать Spring и его модули, но приложение не должно запускаться на локальном веб-сервере.
3. Метод `generateReport()` должен срабатывать по умолчанию.
4. В директории /tests должно быть не менее 3 unit тестов.
5. Ключевые классы должны содержать javadoc описание.
6. Решение должно быть размещено на GitHub в виде проекта и JAR файла с зависимостями.
7. В репозитории должно быть описание задания и вашего решения.

## Критерии

Оцениваться будут:

- Знание Java Core.
- Умение работать с инструментарием экосистемы Java.
- Умение работать с БД.
- Грамотная архитектура решения.
- Оптимальность кода.
- Работоспособность решения.
- Гибкость и расширяемость решения.

## Глоссарий

- **CDR** (*Call Data Record*) – формат файла, содержащего в себе информацию о действиях, совершенных абонентом за тарифицируемый период.
- **BCE** (*Billing and Charging Evolution*) – стандарт обмена роуминговыми данными.
- **UDR** (*Usage Data Report*) - Отчет об использовании данных;
- **msisdn**  - Mobile Subscriber Integrated Services Digital Number - номер мобильного абонента цифровой сети.

# Описание решения

## Схема взаимодействия компонентов

Упрощённая схема взаимодействия компонентов реализованной системы выглядит следующим образом:

![interaction](/img/interaction.png)

## Описание основных компонентов

### Коммутатор (Commutator).
Непосредственная часть предметной области задания. Его основной задачей является получение и предоставление данных о совершенных транзакциях. Выступает в качестве интерфейса взаимодействия между пользователем и системой.

### CDR сервис (CDR service)
Компонент системы, отдельный от коммутатора. Его основной функциональностью является генерация CDR. Также принимает информацию о новых транзакциях и сохраняет их.

### Сервис транзакций (Transaction service)
Предоставляет возможность сохранения и получение транзакций. Также позволяет переместить уже обработанные транзакции в другое место.

### Провайдер транзакций (Date transaction provider)
Компонент системы, ответственный за предоставление информации о транзакциях, совершённых за определённый период.

### UDR сервис (UDR service)
Основной задачей этого сервиса является обработка информации, полученной от **провайдера**, и генерация UDR для каждого пользователя за определённый период.

## Схема базы данных

ERM итоговой схемы выглядит следующим образом:

![schema](/img/schema.png)

## Описание схемы базы данных

### Пользователь (Customer)
Непосредственно абонент, совершивший транзакцию.

### Транзакция (Transaction)
Информация о транзакции, совершённой пользователем.

### Использованная транзакция (Listed transaction)
Сущность, полностью дублирующая все поля **транзакции**. Служит для переноса данных о транзакциях, которые уже были использованы при генерации CDR.