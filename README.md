# Проект "Анализатор страниц"
[![Actions Status](https://github.com/sheykoda-rettani/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/sheykoda-rettani/java-project-72/actions)  [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sheykoda-rettani_java-project-72&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sheykoda-rettani_java-project-72)  [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sheykoda-rettani_java-project-72&metric=bugs)](https://sonarcloud.io/summary/new_code?id=sheykoda-rettani_java-project-72) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sheykoda-rettani_java-project-72&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sheykoda-rettani_java-project-72)
<br/>Проект представляет собой веб приложение для анализа различных сайтов в интернете
## Описание функционала

Приложение позволяет:
1.  **Добавлять URL-адреса** сайтов для последующего анализа.
2.  **Запускать проверку** доступности сайта и собирать SEO-информацию (заголовки `h1`, `title`, `description`).
3.  **Просматривать историю проверок** для каждого добавленного сайта.
4.  **Отслеживать статус** (код ответа сервера) и дату последней проверки для всех сайтов в списке.

## Установка и запуск проекта
Прежде всего установите утилиту `make`. Затем соберите проект :
```shell
  make build 
```

Чтобы запустить проект, используйте следующую команду:
```shell
  make run-dist 
```

## Развернутая версия приложения доступна по ссылке
[Анализатор страниц](https://java-project-72-6zuk.onrender.com)
