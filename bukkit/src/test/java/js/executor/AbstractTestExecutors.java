package js.executor;

import js.AbstractTestJavaScripts;
import js.JsTest;
import js.ExecutorTest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyInt;
import static org.mockito.ArgumentMatchers.any;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import java.util.Collection;
import static io.github.wysohn.triggerreactor.core.utils.TestUtil.*;
import java.util.ArrayList;
import org.bukkit.GameMode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import java.util.Random;
/**
 * Test driving class for testing Executors.
 *
 */
public abstract class AbstractTestExecutors extends AbstractTestJavaScripts {
    @Test
    public void testPlayer_SetFlyMode() throws Exception{
        Player mockPlayer = Mockito.mock(Player.class);
        
        JsTest test = new ExecutorTest(engine, "SETFLYMODE")
                .addVariable("player", mockPlayer);

        for (boolean b : new boolean[] {true, false})
        {
        	test.withArgs(b).test();
        	Mockito.verify(mockPlayer).setAllowFlight(Mockito.eq(b));
        	Mockito.verify(mockPlayer).setFlying(Mockito.eq(b));
        }
        
        assertError(() -> test.withArgs(true, true).test(), "Incorrect number of arguments for executor SETFLYMODE");
        assertError(() -> test.withArgs("merp").test(), "Invalid argument for executor SETFLYMODE: merp");
    }
    
    @Test
    public void testPlayer_SetFlySpeed() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETFLYSPEED")
                .addVariable("player", vp);

        //only case
        test.withArgs(0.5).test();
        Mockito.verify(vp).setFlySpeed(0.5F);

        //Unexpected cases
        assertError(() -> test.withArgs().test(), "Incorrect Number of arguments for Executor SETFLYSPEED");
        assertError(() -> test.withArgs(0.5, 13).test(), "Incorrect Number of arguments for Executor SETFLYSPEED");
        assertError(() -> test.withArgs("HI").test(), "Invalid argument for SETFLYSPEED: HI");
        assertError(() -> test.withArgs(4).test(), "Argument for Executor SETFLYSPEED is outside of range -1..1");
        assertError(() -> test.withArgs(-4).test(), "Argument for Executor SETFLYSPEED is outside of range -1..1");
    }
    
    @Test
    public void testPlayer_SetFood() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETFOOD")
                .addVariable("player", vp);


        //case1
        test.withArgs(3).test();
        Mockito.verify(vp).setFoodLevel(3);

        //case2
        test.withArgs(4.0).test();
        Mockito.verify(vp).setFoodLevel(4);

        //Unexpected Cases
        assertError(() -> test.withArgs().test(), "Incorrect Number of arguments for Executor SETFOOD");
        assertError(() -> test.withArgs("HI").test(), "Invalid argument for Executor SETFOOD: HI");
        assertError(() -> test.withArgs(3.4).test(), "Argument for Executor SETFOOD should be a whole number");
        assertError(() -> test.withArgs(-3.0).test(), "Argument for Executor SETFOOD should not be negative");
    }
    
    @Test
    public void testPlayer_SetGameMode() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETGAMEMODE")
                .addVariable("player", vp);

        //only case
        test.withArgs("creative").test();
        Mockito.verify(vp).setGameMode(GameMode.valueOf("CREATIVE"));

        //Unexpected Cases
        assertError(() -> test.withArgs().test(), "Incorrect number of arguments for executor SETGAMEMODE");
        assertError(() -> test.withArgs(34).test(), "Invalid argument for Executor SETGAMEMODE: 34");
        assertError(() -> test.withArgs("hElLo").test(), "Unknown GAEMMODE value hElLo");
    }
    
    @Test
    public void testPlayer_SetHealth() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETHEALTH")
                .addVariable("player", vp);
        PowerMockito.when(vp, "getMaxHealth").thenReturn(20.0);

        //case1
        test.withArgs(2).test();
        Mockito.verify(vp).setHealth(2.0);

        //case2
        test.withArgs(3.0).test();
        Mockito.verify(vp).setHealth(3.0);

        //Unexpected Cases
        assertError(() -> test.withArgs(1, 334).test(), "Incorrect Number of arguments for executor SETHEALTH");
        assertError(() -> test.withArgs("yeahh").test(), "Invalid argument for SETHEALTH: yeah");
        assertError(() -> test.withArgs(-17).test(), "Argument for Exector SETHEALTH should not be negative");
        assertError(() -> test.withArgs(50).test(),"Argument for Executor SETHEALTH is greater than the max health");
    }
    
    @Test
    public void testPlayer_SetMaxHealth() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETMAXHEALTH")
                .addVariable("player", vp);

        //case1
        test.withArgs(30).test();
        Mockito.verify(vp).setMaxHealth(30.0);

        //case2
        test.withArgs(35.4).test();
        Mockito.verify(vp).setMaxHealth(35.4);

        //Unexpected Cases
        assertError(() -> test.withArgs(20, 33).test(), "Incorrect Number of arguments for Executor SETMAXHEALTH");
        assertError(() -> test.withArgs("NONO").test(), "Invalid argument for SETMAXHEALTH: NONO");
        assertError(() -> test.withArgs(-30).test(), "Argument for Executor SETMAXHEALTH should not be negative or zero");
        assertError(() -> test.withArgs(2098).test(), "Maximum health cannot be greater than 2048");
    }
    
    @Test
    public void testPlayer_SetSaturation() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETSATURATION")
                .addVariable("player", vp);

        //case1
        test.withArgs(25).test();
        Mockito.verify(vp).setSaturation(25.0F);

        //case2
        test.withArgs(44.0).test();
        Mockito.verify(vp).setSaturation(44.0F);

        //Unexpected Cases
        assertError(() -> test.withArgs().test(), "Incorrect Number of arguments for Executor SETSATURATION");
        assertError(() -> test.withArgs("Hi").test(), "Invalid argument for SETSATURATION: Hi");
        assertError(() -> test.withArgs(-45).test(), "Argument for Executor SETSATURATION should not be negative");
    }
    
    @Test
    public void testPlayer_SetWalkSpeed() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETWALKSPEED")
                .addVariable("player", vp);
        //case1
        test.withArgs(1).test();
        Mockito.verify(vp).setWalkSpeed(1.0F);

        //case2
        test.withArgs(0.7).test();
        Mockito.verify(vp).setWalkSpeed(0.7F);

        //Unexpected Cases
        assertError(() -> test.withArgs().test(), "Incorrect Number of arguments for Executor SETWALKSPEED");
        assertError(() -> test.withArgs("NUU").test(), "Invalid argument for SETWALKSPEED: NUU");
        assertError(() -> test.withArgs(-3).test(), "Argument for Executor SETWALKSPEED is outside of the allowable range -1..1");
    }
    
    @Test
    public void testPlayer_SetXp() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "SETXP")
                .addVariable("player", vp);

        //case1
        test.withArgs(0.3).test();
        Mockito.verify(vp).setExp(0.3F);

        //case2
        test.withArgs(1).test();
        Mockito.verify(vp).setExp(1.0F);

        //Unexpected Cases
        assertError(() -> test.withArgs().test(), "Incorrect number of arguments for executor SETXP");
        assertError(() -> test.withArgs("lmao").test(), "Invalid argument for SETXP: lmao");
        assertError(() -> test.withArgs(33).test(), "33 is outside of the allowable range of 0..1 for executor SETXP");



    }

    @Test
//    @PrepareForTest({TriggerReactor.class, Spigot.class, BaseComponent.class, ComponentBuilder.class})
    public void testActionBar() throws Exception{
        /** Player vp = Mockito.mock(Player.class);
        Spigot sp = Mockito.mock(Spigot.class);
        BaseComponent vBaseComponent = Mockito.mock(BaseComponent.class);
        //PowerMockito.mockStatic(ComponentBuilder.class);
        //BaseComponent[] comp = new ComponentBuilder("-").create();
        ChatMessageType msgType = ChatMessageType.ACTION_BAR;
        JsTest test = new ExecutorTest(engine, "ACTIONBAR")
                .addVariable("player", vp)
                .addVariable("component", vBaseComponent);
        PowerMockito.when(vp, "spigot").thenReturn(sp);
        //PowerMockito.when(vCompBuilder, "create").thenReturn(vBaseComponent);
        test.withArgs("-").test();
        Mockito.verify(sp).sendMessage(msgType, vBaseComponent);
         **/

    }
    
    @Test
    public void testBroadcast() throws Exception{
        Collection<Player> players = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            players.add(Mockito.mock(Player.class));
        }
        
        String message = "&cHey all";
        String colored = ChatColor.translateAlternateColorCodes('&', message);
        
        PowerMockito.doReturn(players)
            .when(Bukkit.class, "getOnlinePlayers");
        
        new ExecutorTest(engine, "BROADCAST")
            .withArgs(message)
            .test();
        
        for(Player mockPlayer : players){ 
            Mockito.verify(mockPlayer)
                .sendMessage(Mockito.argThat((String s) -> colored.equals(s)));
        }
    }
    
    @Test
    public void testBurn() throws Exception{
    	//happy cases
    	
    	Player mockPlayer = Mockito.mock(Player.class);
    	Entity mockEntity = Mockito.mock(Entity.class);
    	JsTest test = new ExecutorTest(engine, "BURN").addVariable("player", mockPlayer);
    	
    	test.withArgs(3).test();
    	Mockito.verify(mockPlayer).setFireTicks(60);
    	
    	test.withArgs(0.101).test();
    	Mockito.verify(mockPlayer).setFireTicks(2);
    	
    	test.withArgs(mockEntity, 1).test();
    	Mockito.verify(mockEntity).setFireTicks(20);
    	
    	PowerMockito.when(Bukkit.class, "getPlayer", "merp").thenReturn(mockPlayer);
    	test.withArgs("merp", 5).test();
    	Mockito.verify(mockPlayer).setFireTicks(100);
    	
    	//sad cases
    	PowerMockito.when(Bukkit.class, "getPlayer", "merp").thenReturn(null);
    	assertError(() -> test.withArgs(-1).test(),                 "The number of seconds to burn should be positive");
    	assertError(() -> test.withArgs().test(),                   "Invalid number of parameters. Need [Number] or [Entity<entity or string>, Number]");
    	assertError(() -> test.withArgs(1, 1, 1).test(),            "Invalid number of parameters. Need [Number] or [Entity<entity or string>, Number]");
    	assertError(() -> test.withArgs(true).test(),               "Invalid number for seconds to burn: true");
    	assertError(() -> test.withArgs(null, 4).test(),            "player to burn should not be null");
    	assertError(() -> test.withArgs("merp", 3).test(),          "player to burn does not exist");
    	assertError(() -> test.withArgs(3, 3).test(),               "invalid entity to burn: 3");
    	assertError(() -> test.withArgs(mockEntity, "merp").test(), "The number of seconds to burn should be a number");
    	assertError(() -> test.withArgs(mockEntity, -1).test(),     "The number of seconds to burn should be positive");
    }
    
    @Test
    public void testClearChat() throws Exception{
        Player vp = Mockito.mock(Player.class);
        Player vp2 = Mockito.mock(Player.class);
        Player nullP = null;
        JsTest test = new ExecutorTest(engine, "CLEARCHAT").addVariable("player", vp);
        
        //case1
        test.withArgs().test();
        Mockito.verify(vp, times(30)).sendMessage("");

        //case2
        test.withArgs(vp2).test();
        Mockito.verify(vp2, times(30)).sendMessage("");

        //Unexpected Cases
        assertError(() -> test.withArgs(nullP).test(), "Found unexpected parameter - player: null");
        assertError(() -> test.withArgs(1, 2).test(), "Too many parameters found! CLEARCHAT accept up to one parameter.");
    }
    
    @Test
    public void testClearEntity() throws Exception{
        Player vp = Mockito.mock(Player.class);
        Collection<Entity> entities = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            entities.add(Mockito.mock(Entity.class));
        }
        JsTest test = new ExecutorTest(engine, "CLEARENTITY")
                .addVariable("player", vp);
        PowerMockito.when(vp, "getNearbyEntities", 2d, 2d, 2d).thenReturn(entities);
        test.withArgs(2).test();
        for(Entity ve : entities){
            Mockito.verify(ve).remove();
        }
        assertError(() -> test.withArgs().test(), "Invalid parameters! [Number]");
        assertError(() -> test.withArgs("NO").test(), "Invalid parameters! [Number]");
    }
    
    @Test
    public void testClearPotion() throws Exception{
       /** Player vp = Mockito.mock(Player.class);
        Player vp2 = Mockito.mock(Player.class);
        Collection<PotionEffect> effects = new ArrayList<>();

            effects.add(Mockito.mock(PotionEffect.class));
        JsTest test = new ExecutorTest(engine, "CLEARPOTION")
                .addVariable("player", vp);

        PowerMockito.when(vp, "getActivePotionEffects").thenReturn(effects);
        for(PotionEffect pot : effects){
            PowerMockito.when(pot, "getType").thenReturn(PotionEffectType.BLINDNESS);
        }

        test.withArgs().test();
        for(PotionEffect potion : effects){
            Mockito.verify(vp).removePotionEffect(potion.getType());
        }
        test.addVariable("player", vp2);
        PowerMockito.mockStatic(PotionEffectType.class);
        //PowerMockito.when(PotionEffectType.class, "getByName", "HEAL").thenReturn(PotionEffectType.HEAL);
        test.withArgs("heal").test();
        Mockito.verify(vp2).removePotionEffect(PotionEffectType.getByName("HEAL"));

        assertError(() -> test.withArgs("hello").test(), "Invalid PotionEffectType named HELLO");
        **/
    }
    
    @Test
    public void testCloseGUI() throws Exception{
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "CLOSEGUI")
                .addVariable("player", vp);

        //only happy case
        test.withArgs().test();
        Mockito.verify(vp).closeInventory();
    }
    
    @Test
    public void testCmd() throws Exception{
        // #CMD internally creates Event which cannot be tested
    }
    
    @Test
    public void testCmdCon() throws Exception{
        // #CMDCON retrieve ConsoleCommandSender by static method
    }
    
    @Test
    public void testDoorClose() throws Exception{
        /**Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "DOORCLOSE")
                .addVariable("player", vp);

        World world = Mockito.mock(World.class);
        Location loc = Mockito.mock(Location.class);
        Block bl = Mockito.mock(Block.class);
        BlockState bs = Mockito.mock(BlockState.class);
        Openable oa = Mockito.mock(Openable.class);
        PowerMockito.when(loc.getBlock()).thenReturn(bl);
        PowerMockito.when(bl.getState()).thenReturn(bs);
        PowerMockito.when(bs.getData()).thenReturn(oa);
        test.withArgs(loc).test();
        Mockito.verify(oa).setOpen(false);

        //PowerMockito.when(vp, "getWorld").thenReturn(world2);
        //test.withArgs(1, 3, 5).test();
        //Mockito.verify(world2, 1, 3, 5);
        **/
        //This uses Openable interface, but It has different path between legacy ver and latest ver.
    }
    
    @Test
    public void testDoorOpen() throws Exception{
        //Same as above.
    }
    
    @Test
    public void testDoorToggle() throws Exception{
        //Same as above.
    }
    
    @Test
    public void testDropItem() throws Exception{
        /**
        Player vp = Mockito.mock(Player.class);
        JsTest test = new ExecutorTest(engine, "DROPITEM")
                .addVariable("player", vp);
        //happy case of arg.length == 2
        Location loc1 = Mockito.mock(Location.class);
        ItemStack item1 = Mockito.mock(ItemStack.class);
        World world1 = Mockito.mock(World.class);
        PowerMockito.when(loc1, "getWorld").thenReturn(world1);
        test.withArgs(item1, loc1).test();
        Mockito.verify(world1).dropItem(loc1, item1);

        //happy case of args.length == 4
        //case1 - integer item ID.
        Location loc2 = Mockito.mock(Location.class);
        World world2 = Mockito.mock(World.class);
        PowerMockito.when(loc2, "getWorld").thenReturn(world2);
        ItemStack tempItem = new ItemStack(Material.STONE, 2);
        ItemStack item2 = Mockito.spy(tempItem);
        test.addVariable("ItemStack", item2);
        test.withArgs("STONE", 2, "NONE", loc2).test();
        Mockito.verify(world2).dropItem(loc2, Mockito.any(ItemStack.class));
        **/
    }
    
    @Test
    public void testExplosion() throws Exception{
        World world = Mockito.mock(World.class);
        Location loc = new Location(world, 1, 2, 3);
        Location vLoc = Mockito.spy(loc);
        PowerMockito.when(Bukkit.class, "getWorld", "hello").thenReturn(world);
        JsTest test = new ExecutorTest(engine, "EXPLOSION");
        test.withArgs("hello", 1, 2, 3);
        Mockito.verify(world).createExplosion(vLoc, 4.0F, false);
    }
    
    @Test
    public void testFallingBlock() throws Exception{
        //TODO
    }
    
    @Test
    public void testGive() throws Exception{
        //TODO
    }
    
    @Test
    public void testGUI() throws Exception{
        //TODO
    }
    
    @Test
    public void testItemFrameRotate() throws Exception{
        //TODO
    }
    
    @Test
    public void testItemFrameSet() throws Exception{
        //TODO
    }
    
    @Test
    public void testKill() throws Exception{
        //TODO
    }
    
    @Test
    public void testLeverOff() throws Exception{
        //TODO
    }
    
    @Test
    public void testLeverOn() throws Exception{
        //TODO
    }
    
    @Test
    public void testLeverToggle() throws Exception{
        //TODO
    }
    
    @Test
    public void testLightning() throws Exception{
        //TODO
    }
    
    @Test
    public void testLog() throws Exception{
        //TODO
    }
    
    @Test
    public void testMessage() throws Exception{
        Player mockPlayer = Mockito.mock(Player.class);

        new ExecutorTest(engine, "MESSAGE")
                .addVariable("player", mockPlayer)
                .withArgs("&cTest Message")
                .test();

        String expected = ChatColor.translateAlternateColorCodes('&', "&cTest Message");
        Mockito.verify(mockPlayer).sendMessage(Mockito.argThat((String str) -> expected.equals(str)));
    }
    
    @Test
    public void testModifyHeldItem() throws Exception{
        //TODO
    }
    
    @Test
    public void testModifyPlayer() throws Exception{
        //No longer supported
    }
    
    @Test
    public void testMoney() throws Exception{
        //TODO
    }
    
    @Test
    public void testMysql() throws Exception{
        //TODO
    }
    
    @Test
    public void testPermission() throws Exception{
        //TODO
    }
    
    @Test
    public void testPotion() throws Exception{
        //TODO
    }
    
    @Test
    public void testPush() throws Exception{
        //TODO
    }
    
    @Test
    public void testRotateBlock() throws Exception{
        //TODO
    }
    
    @Test
    public void testScoreboard() throws Exception{
        //TODO
    }
    
    @Test
    public void testServer() throws Exception{
        //TODO
    }
    
    @Test
    public void testSetBlock() throws Exception{
        //TODO
    }
    
    @Test
    public void testSignEdit() throws Exception{
        //TODO        
    }
    
    @Test
    public void testSound() throws Exception{
        //TODO
    }
    
    @Test
    public void testSoundAll() throws Exception{
        //TODO
    }
    
    @Test
    public void testSpawn() throws Exception{
        //TODO
    }
    
    @Test
    public void testTime() throws Exception{
        //TODO
    }
    
    @Test
    public void testTp() throws Exception{
        //TODO
    }
    
    @Test
    public void testTppos() throws Exception{
        //TODO
    }
    
    @Test
    public void testVelocity() throws Exception{
        //TODO
    }
    
    @Test
    public void testWeather() throws Exception{
    	JsTest test = new ExecutorTest(engine, "WEATHER");
        World mockWorld = Mockito.mock(World.class);
        PowerMockito.when(Bukkit.class, "getWorld", "merp").thenReturn(mockWorld);

        test.withArgs("merp", true).test();
        Mockito.verify(mockWorld).setStorm(true);
        
        PowerMockito.when(Bukkit.class, "getWorld", "merp").thenReturn(null);
        assertError(() -> test.withArgs("merp", true, true).test(), "Invalid parameters! [String, Boolean]");
        assertError(() -> test.withArgs("merp", 1).test(), "Invalid parameters! [String, Boolean]");
        assertError(() -> test.withArgs(mockWorld, false).test(), "Invalid parameters! [String, Boolean]");
        assertError(() -> test.withArgs("merp", true).test(), "Unknown world named merp");
    }
    @Test
    public void testKick() throws Exception{

        Player vp = Mockito.mock(Player.class);
        Player vp2 = Mockito.mock(Player.class);
        Player nullP = null;
        String msg = ChatColor.translateAlternateColorCodes('&', "&c[TR] You've been kicked from the server.");
        String msg2 = ChatColor.translateAlternateColorCodes('&', "&cKICKED");

        //case1
        JsTest test = new ExecutorTest(engine, "KICK").addVariable("player", vp);
        test.withArgs().test();
        Mockito.verify(vp).kickPlayer(msg);

        //case2
        test.withArgs(msg2).test();
        Mockito.verify(vp).kickPlayer(msg2);

        //case3
        test.withArgs(vp2).test();
        Mockito.verify(vp2).kickPlayer(msg);

        //case4
        test.withArgs(vp2, msg2).test();
        Mockito.verify(vp2).kickPlayer(msg2);

        //Unexpected Exception Cases
        assertError(() -> test.withArgs(1).test(), "Found unexpected type of argument: 1");
        assertError(() -> test.withArgs(vp, 232).test(), "Found unexpected type of argument(s) - player: "+vp+" | msg: 232");
        assertError(() -> test.withArgs(1 , 2 , 3).test(), "Too many arguments! KICK Executor accepts up to two arguments.");
        test.addVariable("player", null);
        assertError(() -> test.withArgs().test(), "Too few arguments! You should enter at least on argument if you use KICK executor from console.");
        assertError(() -> test.withArgs(null, "msg").test(), "Found unexpected type of argument(s) - player: null | msg: msg");
        assertError(() -> test.withArgs(nullP).test(), "Unexpected Error: parameter does not match - player: null");
    }


}
