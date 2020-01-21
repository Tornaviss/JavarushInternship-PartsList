# JavarushInternship-PartsList
Test task for the Javarush summer internship.

Адрес: "http://localhost:8080/"

Задание: PARTS (компьютерные комплектующие)
Реализовать простенькое приложение Parts-list, для отображения списка деталей для сборки компьютеров на складе. Записи хранить в базе данных. Схему таблички для хранения нужно придумать самому (достаточно одной таблицы).
Нужно показывать список уже имеющихся деталей (с пейджингом по 10 штук на странице). В списке должно быть наименование детали (String), обязательна ли она для сборки (boolean) и их количество на складе (int).
На склад можно добавлять новые детали, редактировать существующие детали (любое из полей), удалять.
• Должна быть сортировка по принципу:
все детали, детали, которые необходимы для сборки, опциональные детали.
• Должен быть поиск по наименованию детали.
Бизнес-требование: ниже списка деталей всегда выводить поле, в котором выводится, сколько компьютеров можно собрать из деталей в наличии.
Для сборки одного компьютера должны быть в наличии все детали, которые отмечены как необходимые.

Для запуска:
1.	Импортировать проект в idea.
2.	Создать базу скриптом init_db.sql (находится в корне). Использовал mysql. Пользователь root, пароль root, база test.
3.	Выполнить mvn clean deploy
4.	В браузере открыть "http://localhost:8080/"

NOTE: Работал очень торопливо, потому не успел описать всё должным образом или пофиксить все баги.  
Помимо стандартных свойств, у объектов Part добавлено поле типа enum определяющее конкретный тип запчасти. Код местами грязноват, но это исправимо.
Буду благодарен за фидбек на Tornaviss@gmail.com
