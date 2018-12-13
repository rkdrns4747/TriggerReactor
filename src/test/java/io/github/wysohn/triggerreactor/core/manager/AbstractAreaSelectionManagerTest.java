package io.github.wysohn.triggerreactor.core.manager;

import io.github.wysohn.triggerreactor.core.main.TriggerReactor;
import io.github.wysohn.triggerreactor.core.manager.location.SimpleLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractAreaSelectionManager.class})
public class AbstractAreaSelectionManagerTest {
    @Mock
    TriggerReactor plugin;

    AbstractAreaSelectionManager manager;
    UUID uuid = UUID.randomUUID();

    SimpleLocation sloc1 = new SimpleLocation("world1", 0, 0, 0);
    SimpleLocation sloc2 = new SimpleLocation("world1", 10, 10, 10);
    SimpleLocation sloc3 = new SimpleLocation("world2", 20, 20, 20);

    @Before
    public void init() throws Exception{
        manager = new AbstractAreaSelectionManager(plugin) {
            @Override
            public void reload() {

            }

            @Override
            public void saveAll() {

            }
        };
    }

    @Test
    public void getSmallest() throws Exception{
        SimpleLocation result = Whitebox.invokeMethod(manager, "getSmallest", sloc1, sloc2);
        Assert.assertEquals(sloc1, result);
    }

    @Test
    public void getLargest() throws Exception{
        SimpleLocation result = Whitebox.invokeMethod(manager, "getLargest", sloc1, sloc2);
        Assert.assertEquals(sloc2, result);
    }

    @Test
    public void onClick() {
        manager.toggleSelection(uuid);

        AbstractAreaSelectionManager.ClickResult result;

        result = manager.onClick(null, uuid, sloc2);
        Assert.assertNull(result);

        result = manager.onClick(AbstractAreaSelectionManager.ClickAction.LEFT_CLICK_BLOCK, uuid, sloc2);
        Assert.assertEquals(sloc2, manager.leftPosition.get(uuid));
        Assert.assertEquals(AbstractAreaSelectionManager.ClickResult.LEFTSET, result);

        result = manager.onClick(AbstractAreaSelectionManager.ClickAction.RIGHT_CLICK_BLOCK, uuid, sloc1);
        Assert.assertEquals(sloc1, manager.rightPosition.get(uuid));
        Assert.assertEquals(AbstractAreaSelectionManager.ClickResult.COMPLETE, result);

        manager.toggleSelection(uuid);
        manager.toggleSelection(uuid);

        result = manager.onClick(AbstractAreaSelectionManager.ClickAction.RIGHT_CLICK_BLOCK, uuid, sloc2);
        Assert.assertEquals(sloc2, manager.rightPosition.get(uuid));
        Assert.assertEquals(AbstractAreaSelectionManager.ClickResult.RIGHTSET, result);

        result = manager.onClick(AbstractAreaSelectionManager.ClickAction.LEFT_CLICK_BLOCK, uuid, sloc3);
        Assert.assertEquals(sloc3, manager.leftPosition.get(uuid));
        Assert.assertEquals(AbstractAreaSelectionManager.ClickResult.DIFFERENTWORLD, result);

        manager.toggleSelection(uuid);
    }

    @Test(expected = RuntimeException.class)
    public void onClickInvalidParameters() {
        manager.onClick(null, null, sloc2);
    }

    @Test(expected = RuntimeException.class)
    public void onClickInvalidParameters2() {
        manager.onClick(null, uuid, null);
    }

    @Test(expected = RuntimeException.class)
    public void onClickInvalidParameters3() {
        manager.onClick(null, null, null);
    }

    @Test
    public void toggleSelection() {
    }

    @Test
    public void resetSelections() {
    }

    @Test
    public void getSelection() {
    }
}