# vezdekod-java-40 
### Запуск программы:
1. Скачать репозиторий
2. Открыть его как проект в Intellij IDEA
3. Перейти в Project Structure (Ctrl + Shift + Alt + S)
4. Перейти в libraries и добавить lwgjl-3.2 (включить Classes и Native Library Locations)
5. Перейти в Modules-Dependencies и отметить lwgjl-3.2
6. Создать конфигурацию, в которой исполняемым файлом будет src/main/java/Game.java
7. Запустить (Shift - F10) 

Картинка для фона, отрисовываемого в окне - back.jpg

### Управление
Прокрутить колёсико мыши вверх / кнопка вверх - увеличить радиус основного круга
Прокрутить колёсико мыши вниз / кнопка вниз - уменшить радиус основного круга
Второй круг постоянно отрисовывается за курсором.

### Описание алгоритма отрисовки многоугольника с количеством углов n
Для удобства будем рисовать правильные многоугольники.

Любой правильный многоугольник можно вписать в окружность.  
А радиусы этой окружности, проведённые к вершинам многоугольника, будут делить окружность на равные дуги.

Можно вычислить градусные дуги, на которые будет делится окружность - 360/n (n - количество углов)

Зная размер этих дуг, можно для каждого получившегося радиуса  
узнать угол его наклона к горизонтальному диаметру.  (Угол от 0 до 360 градусов)

А отсюда из элементарных математических соображений, можно вычислить координаты каждой вершины,  
зная координаты центра окружности и длину радиуса:

```java
x = circle_center.x - Math.cos(Math.toRadians(passed_angle)) * circle_r;
y = circle_center.y - Math.sin(Math.toRadians(passed_angle)) * circle_r;
```

### Отрисовка окружностей
Как я понял, каждая задача должны быть логическим продолжением предыдущей,  
поэтому я отрисовываю окружности как правильные 100-угольники.
Так же, для удобства был создан отдельный класс Circle.

### Определение пересечения окружностей
Реализуется достаточно просто: при известных координатах центров и радиусах,  
вычисляем расстояние между центрами и сравниваем с радиусами.  При этом нужно проверять,  
находится ли вторая окружнсоть ВНЕ основного круга или ВНУТРИ него.  
В итоге, условие пересечения для dist = (расстояние между двумя точками по координатам):
```java
dist <= mainCircle.getRadius() + cursorCircle.getRadius() && dist > mainCircle.getRadius() - cursorCircle.getRadius()
```

### Следование второй окружности за курсором
В функции onFrame я проверяю, находится ли курсор в окне (переменная bool cursorOver)  
И если да, рисую вторую окружность.

Чтобы же она двигалась за курсором, я переопределяю функцию  
onCursorMoved и в ней обновляю координаты центра окружности и её точки.
