# Инструкция по сборке и запуску программы

    Инструкция по сборке
1. Перейдите в корневую папку проекта:
    cd ImagesToChar
2. Скомпилируйте Java-классы в папку target:
    javac -d target src/java/edu/school21/printer/app/App.java src/java/edu/school21/printer/logic/Logic.java
3. Скопируйте ресурсы (изображение) в папку target:
    cp -r src/resources target
4. Создайте JAR-архив:
    jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .

    Инструкция по запуску
1. Перейдите в папку target:
    cd target
2. Запустите программу с указанием символов для белых и чёрных пикселей:
    java -jar images-to-chars-printer.jar '.' '0'
   где:
   - '.' - символ для белых пикселей.
   - '0' - символ для черных пикселей.
    Пример использования
Команда:
    java -jar images-to-chars-printer.jar '.' '0'

Программа выводит изображение на экран в виде двухцветного ASCII-артистического представления.
