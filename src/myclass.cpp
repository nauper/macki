#include <QDebug>
#include <QObject>

class MyClass : public QObject {
public slots:
    void cppSlot(const QString &msg);
};

void MyClass::cppSlot(const QString &msg)  {
    qDebug() << "Called the c++ slot with message:" << msg;
}
