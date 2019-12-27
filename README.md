# AndroidChat
  Программа выполнена в точности как в видео https://youtu.be/RABVeBSPqYo
Это клиент, обменивающийся сообщениями с Firebase, поддерживающий авторизацию участников чата по эл. почте.
От видео проект отличается реализацией преобразования списков, получаемых из базы данных FirebaseDatabase (FirebaseListOptions).
В обновлённой версии AndroidStudio нельзя прописывать параметры FirebaseListOptions в заголовке:
adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference())
Вместо этого создаётся отдельный элемент options, в котором хранятся параметры:
        FirebaseListOptions<Message> options =
                new FirebaseListOptions.Builder<Message>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(), Message.class)
                        .setLayout(R.layout.list_item)
                        .build();
И заголовок выглядит так:
adapter = new FirebaseListAdapter<Message>(options)
  
Существует проблема: на новом устройстве Xiaomi Redmi Note 8 Pro программа вылетает при запуске. На более старой модели
Note 5 программа работает стабильно.
