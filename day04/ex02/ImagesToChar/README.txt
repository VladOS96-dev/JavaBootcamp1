# Инструкция по сборке и запуску программы

    Инструкция по сборке
1. Перейдите в корневую папку проекта:
    cd ImagesToChar
2. Скомпилируйте Java-классы в папку target:
    javac  -cp "./lib/*" src/java/edu/school21/printer/*/*.java -d target
3. Скопируйте ресурсы (изображение) в папку target:
    cp -r src/resources target
4. Создайте JAR-архив:
    jar cfm target/images-to-chars-printer.jar src/manifest.txt -C target .

Инструкция по запуску
    java -jar target/images-to-chars-printer.jar   --white=<COLOR> --black=<COLOR>
Пример использования:
Команда:
    java -jar target/images-to-chars-printer.jar   --white=BLUE --black=YELLOW

    
Программа выводит изображение на экран в виде двухцветного ASCII-артистического представления.
