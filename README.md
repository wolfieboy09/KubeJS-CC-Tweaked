# KubeJS CC
It is for Forge 1.20.1, and no, not making it into Fabric, because I don't know Fabric.

Example of adding to furnaces
```js
// priority: 0

// Visit the wiki for more info - https://kubejs.com/
ComputerCraftEvents.peripheral(event => {
    // First Param: peripheral type
    // Second Param: What block it goes to
    // Note: you can use regex for second param
    
    event.registerPeripheral("furnace", "furnace")
        // This limits the method to 1 tick/call,
        // as the method is scheduled on main thread.
        // The main thread is synced with the block
        .mainThreadMethod("burnTime", (container, direction, arguments) => {
             return container.entityData.getInt("BurnTime")
        })


        // This has no limit on calling
        // however, the method can't access most of the in-world data.
        // For example, accessing the NBT of a tile entity
        .method("say_hi", (container, direction, arguments) => {
            container.up.set("diamond_block")
            return "hi, here's your diamond block"
        })
})
```