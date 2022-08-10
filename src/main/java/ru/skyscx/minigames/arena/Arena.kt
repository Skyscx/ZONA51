package ru.skyscx.minigames.arena

import com.google.common.eventbus.DeadEvent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.Bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Enderman
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import ru.skyscx.minigames.Zona
import ru.skyscx.minigames.other.messages


class Arena (nameA: String, val pos1: Location, val pos2: Location) : Listener {
    enum class ArenaStatus {
        WAIT, LIVE, LOAD
    }
    var PlayersInGame : ArrayList<Player?> = ArrayList()
    var PlayersDeath : ArrayList<Player> = ArrayList()
    var PlayersCT : ArrayList<Player> = ArrayList()
    var PlayersCTpers1 : ArrayList<Player> = ArrayList()
    var PlayersHERO : ArrayList<Player> = ArrayList()
    var PlayersHeroEM : ArrayList<Player> = ArrayList()
    var PlayersHeroAS : ArrayList<Player> = ArrayList()
    var PlayersLIVE : ArrayList<Player> = ArrayList()
    var PlayersAccept : ArrayList<Player> = ArrayList()
    var PlayersS : ArrayList<Player> = ArrayList()
    var PlayerAndEnder: ArrayList<Player> = ArrayList()
    var WinP: ArrayList<Player> = ArrayList()

    var GameStatus : ArenaStatus = ArenaStatus.WAIT
    var SpawnLocationCT : ArrayList<Location> = ArrayList()
    var SpawnLocationHERO : ArrayList<Location> = ArrayList()
    var SpawnLocationEnder : ArrayList<Location> = ArrayList()
    var SpawnLocationALL : ArrayList<Location> = ArrayList()
    var sizeAll: Int = 2 // Сколько игроков нужно для игры
    var MinP: Int = 1
    val LocEnderEnd = Location(getWorld("world"), -263.0, 107.0, 266.0)
    var sizeCT: Int = SpawnLocationCT.size
    var sizeHERO: Int = SpawnLocationHERO.size
    var name = nameA
    val startTime = 60
    val GameTime = 600
    var CheckBlockEnd = 0
    fun AddPlayer(player: Player?) : Boolean {
        if (GameStatus != ArenaStatus.WAIT) return false
        PlayersInGame.add(player)
        if (PlayersInGame.size == sizeAll) {
            loadGame()
        }
        return true
    }
    fun loadGame(){
        peaceful_dif()
        GameStatus = ArenaStatus.LOAD
        sendArenaMessage("Выберите команду. В противном случае игра начнётся автоматически через 1 минуту!")
        startTimer()
        for ((i, player) in PlayersInGame.withIndex()){
            player?.teleport(SpawnLocationALL[i])
            player?.gameMode = GameMode.ADVENTURE
            player?.inventory?.clear()
            val invSelTeam = createInventory(null, 9, ChatColor.BOLD.toString() + "Выбор команды")
            val ct = ItemStack(Material.LAPIS_BLOCK)
            val meta_ct = ct.itemMeta
            meta_ct.displayName = "СПЕЦНАЗ"
            ct.itemMeta = meta_ct
            invSelTeam.addItem(ct)
            val hero = ItemStack(Material.GOLD_BLOCK)
            val meta_hero = hero.itemMeta
            meta_hero.displayName = "ГЕРОИ"
            hero.itemMeta = meta_hero
            invSelTeam.addItem(hero)
            player?.openInventory(invSelTeam)
        }
        sendArenaMessage(messages.STEP1)
    }
    fun TypeHeroSelect(){
        /*Выбор персонажа - реализовать*/
        for ((i, player) in PlayersHERO.withIndex()){
            val invSelPers = createInventory(null, 9, ChatColor.BOLD.toString() + "Выбор персонажа")
            val em = ItemStack(Material.BLAZE_ROD)
            val meta2 = em.itemMeta
            meta2.displayName = "Илон Маск"
            em.itemMeta = meta2
            invSelPers.addItem(em)
            val h_as = ItemStack(Material.STONE)
            val meta3 = h_as.itemMeta
            meta3.displayName = "Арнольд Шварценеггер"
            h_as.itemMeta = meta3
            invSelPers.addItem(h_as)
            player.openInventory(invSelPers)
        }
    }
    fun startGame(){
        normal_dif()
        GameStatus = ArenaStatus.LIVE
        sendCTMessage(messages.CTinfo)
        sendHEROessage(messages.HEROinfo)
        dispatchCommand(getConsoleSender(), "gamerule keepInventory true");
        for ((i, player) in PlayersCT.withIndex()){
            PlayersLIVE.add(player)
            player.teleport(SpawnLocationCT[i])
            player.gameMode = GameMode.SURVIVAL
            player.sendMessage(messages.start_game)
            sendCTTitle("§eИгра началась!","§сНе дайте украсть пришельца!" )
            player.inventory?.clear()
            //Тут выдача оружия
            //Кирка
            val pickaxe = ItemStack(Material.DIAMOND_PICKAXE)
            val meta_pickaxe = pickaxe.itemMeta
            meta_pickaxe.displayName = "Бурильный аппарат"
            pickaxe.itemMeta = meta_pickaxe
            pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5)
            player.inventory?.setItem(0, pickaxe)
            //Меч
            val sword = ItemStack(Material.DIAMOND_SWORD)
            val meta_sword = sword.itemMeta
            meta_sword.displayName = "Ножик из закаленной стали"
            sword.itemMeta = meta_sword
            player.inventory?.setItem(1, sword)

            //БРОНЯ
            val HELMET = ItemStack(Material.DIAMOND_HELMET)
            val meta_HELMET = HELMET.itemMeta
            meta_HELMET.displayName = "Шлем героя"
            HELMET.itemMeta = meta_HELMET
            player.inventory.helmet = HELMET

            val Chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
            val meta_Chestplate = Chestplate.itemMeta
            meta_Chestplate.displayName = "Нагрудник героя"
            Chestplate.itemMeta = meta_Chestplate
            player.inventory.chestplate = Chestplate

            val Leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            val meta_Leggings = Leggings.itemMeta
            meta_Leggings.displayName = "Штаны героя"
            Leggings.itemMeta = meta_Leggings
            player.inventory.leggings = Leggings

            val Boots = ItemStack(Material.DIAMOND_BOOTS)
            val meta_Boots = Boots.itemMeta
            meta_Boots.displayName = "Ботинки героя"
            Boots.itemMeta = meta_Boots
            player.inventory.boots = Boots
        }
        for ((i, player) in PlayersHERO.withIndex()){
            PlayersLIVE.add(player)
            player.teleport(SpawnLocationHERO[i])
            player.gameMode = GameMode.SURVIVAL
            player.sendMessage(messages.start_game)
            sendHEROTitle("§eИгра началась!","§сУкрадите пришельца любым способом!" )
            player.inventory?.clear()
            //Кирка
            val pickaxe = ItemStack(Material.DIAMOND_PICKAXE)
            val meta_pickaxe = pickaxe.itemMeta
            meta_pickaxe.displayName = "Бурильный аппарат"
            pickaxe.itemMeta = meta_pickaxe
            pickaxe.addEnchantment(Enchantment.DIG_SPEED, 5)
            player.inventory?.setItem(0, pickaxe)
            //Меч
            val sword = ItemStack(Material.GOLD_SWORD)
            val meta_sword = sword.itemMeta
            meta_sword.displayName = "Заточка"
            sword.itemMeta = meta_sword
            player.inventory?.setItem(1, sword)
            //Лук
            val BOW = ItemStack(Material.BOW)
            val meta_BOW = BOW.itemMeta
            meta_BOW.displayName = "Арбалет"
            BOW.itemMeta = meta_BOW
            player.inventory?.setItem(2, BOW)
            //Снаряды
            player.inventory.addItem(ItemStack(Material.ARROW, 64))
            //БРОНЯ
            val HELMET = ItemStack(Material.GOLD_HELMET)
            val meta_HELMET = HELMET.itemMeta
            meta_HELMET.displayName = "Шлем героя"
            HELMET.itemMeta = meta_HELMET
            player.inventory.helmet = HELMET

            val Chestplate = ItemStack(Material.GOLD_CHESTPLATE)
            val meta_Chestplate = Chestplate.itemMeta
            meta_Chestplate.displayName = "Нагрудник героя"
            Chestplate.itemMeta = meta_Chestplate
            player.inventory.chestplate = Chestplate

            val Leggings = ItemStack(Material.GOLD_LEGGINGS)
            val meta_Leggings = Leggings.itemMeta
            meta_Leggings.displayName = "Штаны героя"
            Leggings.itemMeta = meta_Leggings
            player.inventory.leggings = Leggings

            val Boots = ItemStack(Material.GOLD_BOOTS)
            val meta_Boots = Boots.itemMeta
            meta_Boots.displayName = "Ботинки героя"
            Boots.itemMeta = meta_Boots
            player.inventory.boots = Boots

            if (player in PlayersHeroEM){
                player.sendMessage(messages.selHEROelonAbout)
                //Персональное оружие - Призыватель молнии
                val BONE = ItemStack(Material.BONE)
                val meta_BONE = BONE.itemMeta
                meta_BONE.displayName = "Призвать молнии"
                BONE.itemMeta = meta_BONE
                player.inventory?.setItem(4, BONE)
            }
            if (player in PlayersHeroAS){
                player.sendMessage(messages.selHEROarnoldAbout)
                //Персональное оружие - Кидать блок камня
                val STONE = ItemStack(Material.STONE)
                val meta_STONE = STONE.itemMeta
                meta_STONE.displayName = "Устроить шторм из камней"
                STONE.itemMeta = meta_STONE
                player.inventory?.setItem(4, STONE)
            }
        }
        for((i, player) in PlayersInGame.withIndex()){
            if (player in PlayersS){
                player?.sendMessage(messages.playersS)

            }
        }
        CreateEnderman()
        GAMEtimer()
    }
    fun CreateEnderman(){
        val world = getServer().getWorld("world")
        val loc = SpawnLocationEnder[0]
        val ender = EntityType.ENDERMAN

        val entity = world.spawnEntity(loc, ender) as Enderman
        entity.setAI(false)

    }

    fun teamWinCT(){
        sendArenaTitle("Спецназ победил!", " ")
        //Тут чот тоже можно приудмать
    }
    fun teamWinHERO(){
        sendArenaTitle("Команда героев поебедила!", " ")
        //Тут чот тоже можно приудмать
    }
    //подумать
    fun teamLoss(player: Player?){

    }

    fun gameEnd(){
        dispatchCommand(getConsoleSender(), "gamerule keepInventory true");

        sendArenaMessage("GAME OVER. THX")
        for(player in PlayersCT){
            SelectLobby(player)
        }
        for(player in PlayersHERO){
            SelectLobby(player)
        }
        PlayersInGame = ArrayList()
        PlayersDeath = ArrayList()
        PlayersCT = ArrayList()
        PlayersCTpers1 = ArrayList()
        PlayersHERO = ArrayList()
        PlayersHeroEM = ArrayList()
        PlayersHeroAS = ArrayList()
        PlayersLIVE = ArrayList()
        PlayersAccept = ArrayList()
        PlayersS = ArrayList()
        PlayerAndEnder = ArrayList()
        WinP = ArrayList()

        peaceful_dif()
        GameStatus = ArenaStatus.WAIT
    }
    fun sendArenaMessage(msg: String){
        for ((i, player) in PlayersInGame.withIndex()){
            player?.sendMessage(msg)
        }
    }
    fun sendCTMessage(msg: String){
        for ((i, player) in PlayersCT.withIndex()){
            player.sendMessage(msg)
        }
    }
    fun sendHEROessage(msg: String){
        for ((i, player) in PlayersHERO.withIndex()){
            player.sendMessage(msg)
        }
    }
    fun sendCTTitle(msg: String, subMsg: String){
        for ((i, player) in PlayersCT.withIndex()){
            player.sendTitle(msg, subMsg, 40, 20, 10)
        }
    }
    fun sendHEROTitle(msg: String, subMsg: String){
        for ((i, player) in PlayersHERO.withIndex()){
            player.sendTitle(msg, subMsg, 40, 20, 10)
        }
    }
    fun peaceful_dif(){
        getWorlds().stream().forEach { world: World ->
            world.difficulty = Difficulty.PEACEFUL
        }
    }
    fun normal_dif(){
        getWorlds().stream().forEach { world: World ->
            world.difficulty = Difficulty.NORMAL
        }
    }

    fun sendArenaTitle(msg: String, subMsg: String){
        for ((i, player) in PlayersInGame.withIndex()){
            player?.sendTitle(msg, subMsg, 40, 20, 10)
        }
    }
    fun addSpawnLocLOAD(x: Double, y: Double, z: Double, move1: Float = 0f, move2: Float = 0f){
        SpawnLocationALL.add(Location(getWorld("world"), x, y, z, move1, move2))

    }
    fun addSpawnLocEnder(x: Double, y: Double, z: Double){
        SpawnLocationEnder.add(Location(getWorld("world"), x, y, z))
    }
    fun delSpawnLocEnder(){
        SpawnLocationEnder.removeAt(0)
    }
    fun updateSpawnLocEnder(x: Int, y: Int, z: Int){
        SpawnLocationEnder.add(Location(getWorld("world"), x.toDouble(), y.toDouble(), z.toDouble()))
    }
    fun addSpawnLocCT(x: Double, y: Double, z: Double, move1: Float = 0f, move2: Float = 0f){
        SpawnLocationCT.add(Location(getWorld("world"), x, y, z, move1, move2))
        sizeCT = SpawnLocationCT.size
    }
    fun addSpawnLocHERO(x: Double, y: Double, z: Double, move1: Float = 0f, move2: Float = 0f){
        SpawnLocationHERO.add(Location(getWorld("world"), x, y, z, move1, move2))
        sizeHERO = SpawnLocationHERO.size
    }
    private fun SelectLobby(player: Player?){
        PlayersInGame.remove(player)
        player?.teleport(Zona.instance?.lobby)
        player?.gameMode = GameMode.ADVENTURE
        player?.inventory?.clear()
        //Сделать выбор игры через инвентарь

    }
    private fun GAMEtimer(){
        val gameTimer: Thread = object : Thread() {
            override fun run() {
                var ctr: Int = GameTime
                while (ctr > 0) {
                    object : BukkitRunnable() {
                        override fun run() {
                            if (CheckBlockEnd ==1){

                                val player = WinP[0]

                                normal_dif()
                                PlayerAndEnder.remove(player)

                                val l = player.location
                                val x = l.blockX
                                val y = l.blockY
                                val z = l.blockZ
                                updateSpawnLocEnder(x,y,z)
                                CreateEnderman()

                                val HELMET = ItemStack(Material.GOLD_HELMET)
                                val meta_HELMET = HELMET.itemMeta
                                meta_HELMET.displayName = "Шлем героя"
                                HELMET.itemMeta = meta_HELMET
                                player.inventory.helmet = HELMET

                                player.removePotionEffect(PotionEffectType.SLOW)
                                sendArenaMessage("${player.name} вернул пришельца на базу!")
                                CheckBlockEnd = 0
                                teamWinHERO()
                                gameEnd()
                                return

                            }else{
                                if (ctr == 1){
                                    teamWinCT()
                                    gameEnd()
                                }
                            }
                        }
                    }.runTask(Zona.plugin)
                    getConsoleSender().sendMessage("1 sec")
                    ctr -= 1
                    try {
                        sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        gameTimer.start()
    }
    private fun startTimer() {
        val thread: Thread = object : Thread() {
            override fun run() {
                var ctr: Int = startTime
                while (ctr > 0) {
                    object : BukkitRunnable() {
                        override fun run() {
                            if (PlayersAccept.size != sizeAll){
                                if (ctr == 11) {
                                    sendArenaTitle("Игра начнётся через 10 секунд", "Выберите команду!")
                                }
                                if (ctr == 6) {
                                    sendArenaTitle("Игра начнётся через 5 секунд", "Выберите команду!")
                                }
                                if (ctr == 4) {
                                    sendArenaTitle("Игра начнётся через 3 секунды", "Выберите команду!")
                                }
                                if (ctr == 3) {
                                    sendArenaTitle("Игра начнётся через 2 секунды", "Выберите команду!")
                                }
                                if (ctr == 2) {
                                    sendArenaTitle("Игра начнётся через 1 секунду", "Приготовтесь!")
                                }
                                if (ctr == 1) {
                                    getConsoleSender().sendMessage("Open ctr ==1")
                                    //Автораспределение по командам если чел сам не выбрал
                                    for ((i, player) in PlayersInGame.withIndex()){
                                        if (player !in PlayersAccept){
                                            if (PlayersCT.size != sizeCT){
                                                if (player != null) {
                                                    PlayersCT.add(player)
                                                }
                                                if (player != null) {
                                                    PlayersS.add(player)
                                                }
                                            }
                                            if (PlayersHERO.size != sizeHERO){
                                                if (player != null) {
                                                    PlayersHERO.add(player)
                                                }
                                                if (player != null) {
                                                    PlayersS.add(player)
                                                }
                                            }
                                        }
                                    }
                                    startGame()
                                }
                            }else{
                                startGame()
                                ctr = 0
                            }
                        }
                    }.runTask(Zona.plugin)
                    getConsoleSender().sendMessage("1 sec")
                    ctr -= 1
                    try {
                        sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        thread.start()
    }
    @EventHandler
    fun OnClick(e: InventoryClickEvent?){
        if (e!!.view.title == ChatColor.BOLD.toString() + "Выбор команды"){
            if (e.currentItem.itemMeta != null){
                if (e.currentItem.itemMeta.displayName == "СПЕЦНАЗ") {
                    val player = e.whoClicked as Player
                    player.sendMessage(messages.selCT)
                    if (GameStatus == ArenaStatus.LOAD){
                        if (player in PlayersHERO){
                            PlayersHERO.remove(player)
                            getConsoleSender().sendMessage("Player  ${player} unsel HERO(SELECT CT)")
                        }
                        PlayersCT.add(player)
                        PlayersCTpers1.add(player)
                        PlayersAccept.add(player)
                        getConsoleSender().sendMessage("Player  ${player} select CT")
                    }
                    e.isCancelled = true
                    player.closeInventory()
                }
                if (e.currentItem.itemMeta.displayName == "ГЕРОИ") {
                    val player = e.whoClicked as Player
                    player.sendMessage(messages.selHERO)
                    if (GameStatus == ArenaStatus.LOAD){
                        if (player in PlayersCT){
                            PlayersCT.remove(player)
                            getConsoleSender().sendMessage("Player  ${player} unsel CT(SELECT HERO)")
                        }
                        PlayersHERO.add(player)
                        player.closeInventory()
                        TypeHeroSelect()
                        getConsoleSender().sendMessage("Player  ${player} SELECT HERO")
                    }
                    e.isCancelled = true

                }
            }
        }
        //ПЕРСОНАЖ ГЕРОИ
        if (e.view.title == ChatColor.BOLD.toString() + "Выбор персонажа"){
            if (e.getCurrentItem().getItemMeta() != null){
                if (e.currentItem.itemMeta.displayName == "Илон Маск") {
                    val player = e.whoClicked as Player
                    player.sendMessage(messages.selHEROelon)
                    if (GameStatus == ArenaStatus.LOAD){
                        if (player in PlayersHeroAS){
                            PlayersHeroAS.remove(player)
                            getConsoleSender().sendMessage("Player  $player (HERO) unsel AS(SEL EM)")
                        }
                        PlayersHeroEM.add(player)
                        PlayersAccept.add(player)
                        getConsoleSender().sendMessage("Player  $player (HERO) sel EM")
                    }
                    e.isCancelled = true
                    player.closeInventory()
                }
                if (e.currentItem.itemMeta.displayName == "Арнольд Шварценеггер") {
                    val player = e.whoClicked as Player
                    player.sendMessage(messages.selHEROarnold)
                    if (GameStatus == ArenaStatus.LOAD){
                        if (player in PlayersHeroEM){
                            PlayersHeroEM.remove(player)
                            getConsoleSender().sendMessage("Player  $player (HERO) unsel EM(SEL AS")
                        }
                        PlayersAccept.add(player)
                        PlayersHeroAS.add(player)
                        getConsoleSender().sendMessage("Player  $player (HERO) sel AS")
                    }
                    e.isCancelled = true
                    player.closeInventory()
                }
            }
        }
        if (e.currentItem.itemMeta.displayName == "Призвать молнии") {
            val player = e.whoClicked as Player
                if (e.action.equals(Action.RIGHT_CLICK_BLOCK) || e.action.equals(Action.RIGHT_CLICK_AIR)) {
                    sendCTMessage(messages.lighting)
                    sendHEROessage(messages.use_Light)
                    STRIKE_P()
                    return
                }
            sendCTMessage(messages.lighting)
            sendHEROessage(messages.use_Light)
            STRIKE_P()
            }
            e.isCancelled = true
        if (e.currentItem.itemMeta.displayName == "Устроить шторм из камней") {
            val player = e.whoClicked as Player
            if (e.action.equals(Action.RIGHT_CLICK_BLOCK) || e.action.equals(Action.RIGHT_CLICK_AIR)) {
                sendCTMessage(messages.stone_sht)
                sendHEROessage(messages.use_Stone_sht)
                Stone_Shtorm()
                return
            }
            sendCTMessage(messages.lighting)
            sendHEROessage(messages.use_Stone_sht)
            Stone_Shtorm()
        }
        e.isCancelled = true
        }


    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player: Player = event.player
        if (player in PlayerAndEnder) {
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 999999999, 1))
        }
    }


    @EventHandler
    fun RespawnEvent(event: EntityDeathEvent) {
        event.entity.health = 20.0
        val p = event.entity
        p.sendMessage("Вы возраждены!")
        if (p in PlayersCT){
            p.teleport(SpawnLocationCT[0])
        }
        if (p in PlayersHERO){
            p.teleport(SpawnLocationHERO[0])
        }

    }
    @EventHandler
    fun playerDamage(event: EntityDamageByEntityEvent){
        val damager = event.damager
        /*
        if(damager is Player && event.entity is Player){
            return
        }
        * */
        if (damager in PlayersCT || damager in PlayersHERO){
            val player: Player = event.entity as Player
            if (event.entity in PlayerAndEnder){
                normal_dif()
                PlayerAndEnder.remove(player)

                val l = player.location
                val x = l.blockX
                val y = l.blockY
                val z = l.blockZ
                updateSpawnLocEnder(x,y,z)
                CreateEnderman()

                val HELMET = ItemStack(Material.GOLD_HELMET)
                val meta_HELMET = HELMET.itemMeta
                meta_HELMET.displayName = "Шлем героя"
                HELMET.itemMeta = meta_HELMET
                player.inventory.helmet = HELMET

                player.removePotionEffect(PotionEffectType.SLOW)
                sendArenaMessage("${damager.name} скинул пришельца с ${player.name}")
                return
            }
            event.isCancelled = true
        }
        event.isCancelled = true
    }
    @EventHandler
    fun EnderDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if(event.damager is Player && event.entity is Enderman){
            if (damager in PlayersHERO){
                event.isCancelled = true
                sendArenaMessage("Игрок ${damager.name} забирает пришельца.")
                val player: Player = event.damager as Player
                val HELMET_DRAG = ItemStack(Material.BANNER)
                val meta_HELMET_DRAG = HELMET_DRAG.itemMeta
                meta_HELMET_DRAG.displayName = "Сумка с пришельцем"
                HELMET_DRAG.itemMeta = meta_HELMET_DRAG
                player.inventory?.helmet = HELMET_DRAG
                PlayerAndEnder.add(player)
                peaceful_dif()
                delSpawnLocEnder()
            }
            event.isCancelled = false
        }
        event.isCancelled = false

    }
    @EventHandler
    fun CheckBlockEnder(event: PlayerMoveEvent){
        val player: Player = event.player

        if (player in PlayerAndEnder){
            val l = player.location
            val w = getServer().getWorld("world")
            val x = l.blockX
            val y = l.blockY-1
            val z = l.blockZ
            val l2 = Location(w, x.toDouble(), y.toDouble(), z.toDouble())



            if (l2 == LocEnderEnd){
                CheckBlockEnd = 1
                WinP.add(player)
            }
        }


    }

    private fun Stone_Shtorm(){
        val stone_shtorm: Thread = object : Thread() {
            override fun run() {

                object : BukkitRunnable() {
                    override fun run() {
                        if (PlayersAccept.size!= MinP){
                            val rnds = (0..4).random()
                            val player = PlayersCT[0]
                            val l = player.location
                            val x2 = l.blockX+2
                            val x1 = l.blockX-1
                            val y2 = l.blockY+2
                            val y1 = l.blockY-1
                            val z2 = l.blockZ+2
                            val z1 = l.blockZ-1

                            var location: Location
                            for (x in x1..x2) {
                                for (y in y1..y2) {
                                    for (z in z1..z2) {
                                        location = Location(pos1.world, x.toDouble(), y.toDouble(), z.toDouble())
                                        if (location.block.type == Material.AIR){
                                            location.block.type = Material.COBBLESTONE
                                        }
                                    }
                                }
                            }

                            val thread2: Thread = object : Thread() {
                                override fun run() {
                                    val startTime = 8
                                    var ctr: Int = startTime
                                    while (ctr > 0) {
                                        object : BukkitRunnable() {
                                            override fun run() {
                                                if (ctr == 1){
                                                    var location: Location
                                                    for (x in x1..x2) {
                                                        for (y in y1..y2) {
                                                            for (z in z1..z2) {
                                                                location = Location(pos1.world, x.toDouble(), y.toDouble(), z.toDouble())
                                                                if (location.block.type == Material.COBBLESTONE){
                                                                    location.block.type = Material.AIR
                                                                }
                                                            }
                                                        }
                                                    }
                                                }


                                            }
                                        }.runTask(Zona.plugin)

                                        ctr -= 1
                                        try {
                                            sleep(1000)
                                        } catch (e: InterruptedException) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            }
                            thread2.start()

                        }

                    }//getPlugin<Zona>(Zona::class.java)
                }.runTask(Zona.plugin)



            }
        }
        stone_shtorm.start()
    }
    private fun STRIKE_P() {
        val strike_thr: Thread = object : Thread() {
            override fun run() {

                object : BukkitRunnable() {
                    override fun run() {
                        if (PlayersAccept.size!= MinP){
                            val rnds = (0..4).random()
                            val player = PlayersCT[rnds]
                            val l = player.location
                            player.world.strikeLightning(l)

                        }

                    }//getPlugin<Zona>(Zona::class.java)
                }.runTask(Zona.plugin)



            }
        }
        strike_thr.start()
    }
}