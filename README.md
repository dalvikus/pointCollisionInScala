# pointCollisionInScala
Scala version of pointCollision

written in Scala; [C++ version](https://github.com/dalvikus/pointCollision)

## How to Build
```
# create directory for classes
$ mkdir classes

# compile
$ scalac -d classes Interval.scala
$ scalac -cp classes -d classes Node.scala
$ scalac -cp classes -d classes TestNode.scala

# run
$ scala -cp classes TestNode
```
