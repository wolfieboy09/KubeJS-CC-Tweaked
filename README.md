# KubeJS CC
It is for Forge 1.20.1, and no, not making it into Fabric, because I don't know Fabric.

# Tips
Scripts must be put inside the `startup_scripts` folder. You can name them however you want, as long as their extension is `.js`. People on Windows: beware of ghost `.txt` extensions.

### Reloading a script after making changes
Don't restart your game/server! You can reload your changes by executing in order:
- /kubejs reload startup_scripts
- /reload
- Restart the turtle/computer, and, if your peripheral is connected through a wired modem, restart also the peripheral modem by right-clicking twice on the modem.
- Enjoy seeing your new changes!

### Code/syntax issues may not appear inside the game
Keep an eye on your minecraft `latest.log` logfile, it may be the only place where it displays errors you want to avoid occurring.

### What happens if a block has more than one peripheral?
That happens a lot! We got you covered: our mod will merge them automagically. The peripheral type will consequently be named `multiperipheral`.

We also took care of generic peripherals provided natively by Tweaked:CC, but peripherals provided by other mods may be overriden if a block happens to be matched with one of your custom KJS:CC peripherals. Or the other way around. We will take a look into resolving that in a later version of the mod. Please open github issues whenever you encounter a problematic case.

Beware of naming your methods… methodically. Because methods having the same name on a given block will override randomly each other.

### Peripherals can disconnect randomly when they send updates to neighboring blocks
Make sure your Lua code handles cases when the method returns an error (use pcall to catch them), or when they happen to return `nil`/`false` instead of the expected result. If you use Gregtech Modern, make sure to use their [latest build](https://github.com/GregTechCEu/GregTech-Modern/releases) (or a version higher than `1.2.0.a`), they recently [merged a fix resolving an issue causing massive neighbor updates every tick](https://github.com/GregTechCEu/GregTech-Modern/pull/1164).

## Docs
`ComputerCraftEvents.peripheral(function (event) {})` has to be put inside a KubeJS startup script.

__Event (PeripheralRegisterEvent) functions:__

- `event.registerPeripheral(type, blockStateMatcher)`<br>
`type: string` The peripheral type name of your choice<br>
`blockStateMatcher: string | RegExp` A string or regular expression to match against one or multiple block ids (e.g.: `"minecraft:furnace"` or `/^modid:.*_pulverizer$/`).

- `event.registerComplexPeripheral(type, blockTester)`<br>
`type: string` The peripheral type name of your choice<br>
`blockTester: function(BlockContainerJS block) {}` A function that will test the block ([BlockContainerJS](https://github.com/KubeJS-Mods/KubeJS/blob/2001/common/src/main/java/dev/latvian/mods/kubejs/level/BlockContainerJS.java)) against one ore more conditions to write inside. The provided function must return `true` or `false` to be valid.

Both of those Event functions return a Peripheral (see next section below).

__Peripheral (PeripheralJS) functions:__
- `method(name, methodFunction)`
- `mainThreadMethod(name, methodFunction)`

Both of these functions share the same parameters:<br>
`name: string`: the name of the method you want to declare on this peripheral<br>
`methodFunction: function(block, side, args, computer, context) {}`: the function code that will be executed when the peripheral method is called by a turtle/computer.<br>

`methodFunction` function parameter types:<br>
- `block: BlockContainerJS`: the peripheral block, see KubeJS [BlockContainerJS](https://github.com/KubeJS-Mods/KubeJS/blob/2001/common/src/main/java/dev/latvian/mods/kubejs/level/BlockContainerJS.java) code.<br>
- `side: Direction`: the side from which the peripheral is attached, see minecraftforge [Direction](https://nekoyue.github.io/ForgeJavaDocs-NG/javadoc/1.18.2/net/minecraft/core/Direction.html) java docs. In your JS code you can also use `Direction` global variable provided by KubeJS ([code](https://github.com/KubeJS-Mods/Rhino/blob/1.20/main/common/src/main/java/dev/latvian/mods/rhino/mod/wrapper/DirectionWrapper.java))<br>
- `args: array[]`: a javascript Array containing the arguments provided when calling the method<br>
- `computer: IComputerAccess`: the computer instance from which the method was called, you shouldn't need to use it, but it's there if you need it. See [IComputerAccess](https://github.com/cc-tweaked/CC-Tweaked/blob/mc-1.20.x/projects/core-api/src/main/java/dan200/computercraft/api/peripheral/IComputerAccess.java) java code.<br>
`context: ILuaContext`: lua context instance in case you need it at some point (you won't). See [ILuaContext](https://github.com/cc-tweaked/CC-Tweaked/blob/mc-1.20.x/projects/core-api/src/main/java/dan200/computercraft/api/lua/ILuaContext.java) java code.
<br><br>
# Script examples
## Vanilla
Example of methods you could add to furnaces:
```js
// priority: 0
// The following code was last updated: April 30th 2024

// Visit the wiki for more info - https://kubejs.com/
ComputerCraftEvents.peripheral(event => {
    // First Param: peripheral type
    // Second Param: What block it goes to
    // Note: you can use regex for second param
    
    event.registerPeripheral("furnace", "minecraft:furnace")
        // This limits the method to 1 call/tick,
        // as the method is scheduled on main thread.
        // The main thread is synced with the block
        .mainThreadMethod("burnTime", (container, direction, arguments) => {
            // This custom method returns the current
            // remaining burntime the furnace has.
            return container.entityData.getInt("BurnTime")
        })
        .mainThreadMethod("cookingProgress", (container) => {
            // This custom method returns the percentage
            // of the current cooking process going on.
            // A progress of 0 returned during two consecutive
            // ticks means that there is no cooking happening.
            let data = container.entityData
            let cookTime = data.getInt('CookTime')
            let cookTimeTotal = data.getInt('CookTimeTotal')
            if (!cookTimeTotal) return 0;
            return (cookTime / cookTimeTotal) * 100
        })


        // This has no limit on calling
        // however, the method can't access most of the in-world data.
        // For example, it couldn't access the NBT of a tile entity
        .method("say_hi", (container, direction, arguments) => {
            container.up.set("diamond_block")
            return "hi, here's your diamond block"
        })
})
```

## GregTech Modern (GTCEu)
Example of methods you could add to **Gregtech Modern (GTCEu) machines**:

_Note: You do not need to copy/paste the entire script below, feel free to cherrypick which peripherals/methods you want to have. You can also make ones of your own, imagination is the limit, that, and [GTCEu sourcecode](https://github.com/GregTechCEu/GregTech-Modern/tree/1.20.1/src/main/java/com/gregtechceu/gtceu)._
```js
// priority: 0
// The following code was last updated: April 30th 2024

const GTCapabilityHelper = Java.loadClass("com.gregtechceu.gtceu.api.capability.GTCapabilityHelper")
const IntCircuitBehaviour = Java.loadClass("com.gregtechceu.gtceu.common.item.IntCircuitBehaviour")
const LargeTurbineMachine = Java.loadClass("com.gregtechceu.gtceu.common.machine.multiblock.generator.LargeTurbineMachine")
const RotorHolderPartMachine = Java.loadClass("com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine")

// This function is a shortcut allowing to get
// directly the MetaMachine field without having
// to repeat the same code inside our testers/methods
function metaMachineWrapper (cb) {
    return function (block, dir, args, computer, ctx) {
        if (!block || !block.entity || !block.entity.metaMachine) return false
        return cb(block.entity.metaMachine, block, dir, args, computer, ctx)
    }
}

// Also a shortcut to get/find the RotorHolder
// among the large turbine multiblock parts.
function getRotorHolder (turbineMachine) {
    if (!(turbineMachine instanceof LargeTurbineMachine)) return null

    for (let part of turbineMachine.getParts()) {
        if (part.getClass() == RotorHolderPartMachine) {
          return part
        } 
    } 
    return null
}

ComputerCraftEvents.peripheral(event => {
    // Example use of the registerPeripheral method by
    // providing a regex pattern to match GTCEu wires & cables
    event.registerPeripheral("gt_cable", /^gtceu:.*_(wire|cable)$/)
        .mainThreadMethod("getAverageVoltage", (block) => {
            if (!block.entity) return 0;
            return block.entity.averageVoltage
        })
        .mainThreadMethod("getAverageAmperage", (block) => {
            if (!block.entity) return 0;
            return block.entity.averageAmperage
        })
        .mainThreadMethod("getAverageFlowingCurrent", (block) => {
            if (!block.entity) return 0;
            return block.entity.averageVoltage * block.entity.averageAmperage
        })

        // registerComplexPeripheral is another way of registering
        // a peripheral toward many blocks sharing a capability/feature
        // checked inside a custom test function returning only true or false.
        event.registerComplexPeripheral("gt_energy_container", metaMachineWrapper((machine) => {
            return !!(machine.energyContainer)
        }))
        .mainThreadMethod("getEnergyStored", metaMachineWrapper((machine) => {
            return machine.energyContainer.energyStored
        }))
        .mainThreadMethod("getEnergyCapacity", metaMachineWrapper((machine) => {
            return machine.energyContainer.energyCapacity
        }))
        .mainThreadMethod("getOutputPerSec", metaMachineWrapper((machine) => {
            return machine.energyContainer.getOutputPerSec()
        }))
        .mainThreadMethod("getInputPerSec", metaMachineWrapper((machine) => {
            return machine.energyContainer.getInputPerSec()
        }))

        // GTCapabilityHelper.getXXX() is another handy way to get
        // capability handlers directly, but we suspect it might
        // take more resources than our custom metaMachineWrapper.
        event.registerComplexPeripheral("gt_workable", (block) => !!GTCapabilityHelper.getWorkable(block.level, block.pos, null))
            .mainThreadMethod("getProgress", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).progress)
            .mainThreadMethod("getMaxProgress", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).maxProgress)
            .mainThreadMethod("isActive", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).isActive())

        event.registerComplexPeripheral("gt_controllable", (block) => !!GTCapabilityHelper.getControllable(block.level, block.pos, null))
            .mainThreadMethod("isWorkingEnabled", (block, dir) => GTCapabilityHelper.getControllable(block.level, block.pos, dir).isWorkingEnabled())
            .mainThreadMethod("setWorkingEnabled", (block, dir, args) => GTCapabilityHelper.getControllable(block.level, block.pos, dir).setWorkingEnabled(!!args[0]) || "OK")

        event.registerComplexPeripheral("gt_overclockable", (block) => !!GTCapabilityHelper.getWorkable(block.level, block.pos, null))
        .mainThreadMethod("getOverclockTier", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).getOverclockTier())
        .mainThreadMethod("getOverclockVoltage", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).getOverclockVoltage())
        .mainThreadMethod("getMaxOverclockTier", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).getMaxOverclockTier())
        .mainThreadMethod("getMinOverclockTier", (block, dir) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).getMinOverclockTier())
        .mainThreadMethod("setOverclockTier", (block, dir, args) => GTCapabilityHelper.getWorkable(block.level, block.pos, dir).setOverclockTier(toInt(args[0])) || "OK")

        // This one feels like magic: it allows to get/set the circuit number
        // used by the machine / bus. Keep in mind that `-1` means no circuit.
        event.registerComplexPeripheral("gt_circuit_machine", metaMachineWrapper((machine) => {
            return !!machine && !!machine.getCircuitInventory
        }))
        .mainThreadMethod("getProgrammedCircuit", metaMachineWrapper((machine) => {
            const stack = machine.getCircuitInventory().storage.getStackInSlot(0)
            if (stack == Item.empty) return -1;
            return IntCircuitBehaviour.getCircuitConfiguration(stack)
        }))
        .mainThreadMethod("setProgrammedCircuit", metaMachineWrapper((machine, block, _, args) => {
            const storage = machine.getCircuitInventory().storage
            if (args[0] == -1)
                storage.setStackInSlot(0, Item.empty)
            else
                storage.setStackInSlot(0, IntCircuitBehaviour.stack(toInt(args[0])))

            storage.onContentsChanged(0);
            return "OK"
        }))

        event.registerComplexPeripheral("gt_distinct_part", metaMachineWrapper((machine) => {
            return !!machine && !!machine.setDistinct
        }))
        .mainThreadMethod("isDistinct", metaMachineWrapper(machine => machine.isDistinct()))
        .mainThreadMethod("setDistinct", metaMachineWrapper((machine, block, _, args) => machine.setDistinct(!!args[0]) || "OK"))

        // This one is the most complex one, it allows to manage
        // a large turbine (steam/gas/plasma) and get information
        // about its rotor power/durability/efficiency/speed.
        // Coming soon: a method allowing to replace the Rotor remotely
        // from another peripheral inventory.
        event.registerComplexPeripheral("gt_turbine_rotor", metaMachineWrapper((machine) => {
            return !!machine && (machine instanceof LargeTurbineMachine)
        }))
        .mainThreadMethod("getOverclockVoltage", metaMachineWrapper((machine) => {
            return machine.getOverclockVoltage()
        }))
        .mainThreadMethod("getCurrentProduction", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            if (!rotor) return 0;
            let voltage = machine.getOverclockVoltage()
            let speed = rotor.getRotorSpeed()
            let maxSpeed = rotor.getMaxRotorHolderSpeed()
            if (speed >= maxSpeed) return voltage

            return Math.floor(voltage * JavaMath.pow(speed / maxSpeed, 2))
        }))
        .mainThreadMethod("getRotorDurability", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            return rotor && rotor.getRotorDurabilityPercent()
        }))
        .mainThreadMethod("hasRotor", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            return rotor && rotor.hasRotor()
        }))
        .mainThreadMethod("getRotorEfficiency", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            return rotor && rotor.getRotorEfficiency()
        }))
        .mainThreadMethod("getRotorPower", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            return rotor && rotor.getRotorPower()
        }))
        .mainThreadMethod("getRotorSpeed", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            return rotor && rotor.getRotorSpeed()
        }))
        .mainThreadMethod("getMaxRotorSpeed", metaMachineWrapper((machine) => {
            const rotor = getRotorHolder(machine)
            return rotor && rotor.getMaxRotorHolderSpeed()
        }))
})
```