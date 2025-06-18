# Проєктування бази даних

Цей розділ описує структуру даних, які будуть використовуватися системою MediaInsight. Він включає модель бізнес-об'єктів, ER-модель та описує їхню реляційну схему. Ці моделі є фундаментом для проектування бази даних та забезпечення цілісності й ефективності зберігання інформації.

## 1. Модель бізнес-об'єктів

Модель бізнес-об'єктів представляє основні сутності та концепції, з якими оперує система MediaInsight у контексті її функціональності. Вона відображає взаємодію між основними елементами бізнес-процесів.

```
@startuml
!theme mars

entity role {
  + id: UUID
  --
  name: string
  description: string
}

entity user {
  + id: UUID
  --
  name: string
  email: string
  password: string
  role_id: UUID
}

entity media_content {
  + id: UUID
  --
  title: string
  description: string
  type: string
  file_path: string
  user_id: UUID
}

entity project {
  + id: UUID
  --
  name: string
  description: string
  user_id: UUID
}

entity analysis_task {
  + id: UUID
  --
  name: string
  status: string
  create_at: datetime
  user_id: UUID
  project_id: UUID
}

entity task_content {
  + media_content_id: UUID
  + analysis_task_id: UUID
  --
}

entity report {
  + id: UUID
  --
  name: string
  content: string
  create_at: datetime
  analysis_task_id: UUID
}

user "1" -- "0..*" role
user "1" -- "0..*" media_content
user "1" -- "0..*" project
user "1" -- "0..*" analysis_task
project "1" -- "0..*" analysis_task
analysis_task "1" -- "0..*" report

media_content "0..*" -- "0..*" task_content
analysis_task "0..*" -- "0..*" task_content

@enduml
```

## 2. ER-модель (Модель "сутність-зв'язок")

ER-модель деталізує структуру даних, показуючи сутності, їхні атрибути та типи зв'язків між ними, що є основою для подальшого проектування реляційної бази даних.

```
@startuml
!define PRIMARY_KEY_COLOR #FFD700
!define FOREIGN_KEY_COLOR #87CEEB

entity "role" as role_tbl {
  + id: INT <<PRIMARY_KEY_COLOR>>
  --
  name: VARCHAR(64)
  description: TEXT
}

entity "user" as user_tbl {
  + id: INT <<PRIMARY_KEY_COLOR>>
  --
  name: VARCHAR(64)
  email: VARCHAR(128)
  password: VARCHAR(128)
  role_id: INT <<FOREIGN_KEY_COLOR>>
}

entity "media_content" as media_content_tbl {
  + id: INT <<PRIMARY_KEY_COLOR>>
  --
  title: VARCHAR(128)
  description: TEXT
  type: VARCHAR(32)
  file_path: VARCHAR(128)
  user_id: INT <<FOREIGN_KEY_COLOR>>
}

entity "project" as project_tbl {
  + id: INT <<PRIMARY_KEY_COLOR>>
  --
  name: VARCHAR(64)
  description: TEXT
  user_id: INT <<FOREIGN_KEY_COLOR>>
}

entity "analysis_task" as analysis_task_tbl {
  + id: INT <<PRIMARY_KEY_COLOR>>
  --
  name: VARCHAR(64)
  status: VARCHAR(64)
  create_at: DATETIME
  user_id: INT <<FOREIGN_KEY_COLOR>>
  project_id: INT <<FOREIGN_KEY_COLOR>>
}

entity "task_content" as task_content_tbl {
  + media_content_id: INT <<FOREIGN_KEY_COLOR>>
  + analysis_task_id: INT <<FOREIGN_KEY_COLOR>>
  --
}

entity "report" as report_tbl {
  + id: INT <<PRIMARY_KEY_COLOR>>
  --
  name: VARCHAR(64)
  content: TEXT
  create_at: DATETIME
  analysis_task_id: INT <<FOREIGN_KEY_COLOR>>
}

user_tbl "1" -- "0..*" role_tbl
user_tbl "1" -- "0..*" media_content_tbl
user_tbl "1" -- "0..*" project_tbl
user_tbl "1" -- "0..*" analysis_task_tbl
project_tbl "1" -- "0..*" analysis_task_tbl
analysis_task_tbl "1" -- "0..*" report_tbl

media_content_tbl "0..*" -- "0..*" task_content_tbl
analysis_task_tbl "0..*" -- "0..*" task_content_tbl

@enduml
```

## 3. Реляційна схема

Реляційна схема є деталізованим представленням ER-моделі, адаптованим для конкретної реляційної бази даних. Вона визначає таблиці, їхні колонки, типи даних, первинні та зовнішні ключі, а також обмеження, що забезпечують цілісність даних.

![image](/docs/Реляційна_схема.png)

