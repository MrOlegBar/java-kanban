package manager.test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();;

    @Override
    HistoryManager createManager() {
        return historyManager;
    }
}