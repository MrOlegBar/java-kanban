package manager.test;

import manager.HistoryManager;
import org.junit.jupiter.api.Test;

abstract class HistoryManagerTest<T extends HistoryManager> {

    @Test
    public abstract void add();

    @Test
    abstract void getHistory();

    @Test
    abstract void remove();
}