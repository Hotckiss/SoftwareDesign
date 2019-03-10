package ru.roguelike.models.objects.base;

public abstract class AbstractArtifact extends AbstractGameObject {
    //сколько воостановит здоровья
    protected int restoringHealth;
    //сколько добавит к регенерации
    protected int regenerationBonus;
    //сколько добавит шанса к нанесению урона огнем
    protected double fireProbabilityBonus;
    //сколько добавит шанса к заморозке
    protected double freezeProbabilityBonus;
    //насколько увеличит множитель урона
    protected double physicalDamageMultiplierBonus;
    //насколько увеличит множитель урона огнем
    protected double fireDamageMultiplierBonus;
    protected boolean isTaken = false;

    public void take() {
        isTaken = true;
    }

    public boolean taken() {
        return isTaken;
    }
}
