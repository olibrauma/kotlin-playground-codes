import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice(val name: String, val category: String) {

    var deviceStatus = "online"
    	protected set
    
    open val deviceType = "unknown"
    
    open fun turnOn() {
        deviceStatus = "on"
    }
    
    open fun turnOff() {
        deviceStatus = "off"
    }
    
    fun printDeviceInfo() {
        println("Device name: $name, category: $category, type: $deviceType, status: $deviceStatus")
    }
}

class SmartTvDevice(deviceName: String, deviceCategory: String) :
	SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart TV"
    
    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)
    
    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)
    
    fun increaseSpeakerVolume() {
        when (deviceStatus) {
            "on" -> {
                speakerVolume++
                println("Speaker volume increased to $speakerVolume.")
            }
            else -> println("Volume was not increased as the smart TV device isn't on.")
        }
    }
    
    fun decreaseSpeakerVolume() {
    	when (deviceStatus) {
            "on" -> {
                speakerVolume--
                println("Speaker volume decreased to $speakerVolume.")
            }
            else -> println("Volume was not decreased as the smart TV device isn't on.")
        }
    }
    
    fun nextChannel() {
         when (deviceStatus) {
            "on" -> {
                channelNumber++
                println("Channel number increased to ${channelNumber}.")
            }
            else -> println("Channel number was not increased as the smart TV device isn't on.")
        }
    }
    
    fun previousChannel() {
         when (deviceStatus) {
            "on" -> {
                channelNumber--
                println("Channel number increased to ${channelNumber}.")
            }
            else -> println("Channel number was not decreased as the smart TV device isn't on.")
        }
    }
    
    override fun turnOn() {
        super.turnOn()
        println(
        	"$name is turned on. Speaker volume is set to $speakerVolume and " +
                "channel number is $channelNumber."
        )
    }
    
    override fun turnOff() {
        super.turnOff()
        println("$name turned off.")
    }
}
    
class SmartLightDevice(deviceName: String, deviceCategory: String) : 
	SmartDevice(name = deviceName, category = deviceCategory) {
    
    override val deviceType = "Smart Light"    
    
    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)
    
    fun increaseBrightness() {
         when (deviceStatus) {
            "on" -> {
                brightnessLevel++
                println("Brightness level increased to $brightnessLevel.")
            }
            else -> println("Brightness level was not increased as the smart light device isn't on.")
        }
    }

    fun decreaseBrightness() {
         when (deviceStatus) {
            "on" -> {
                brightnessLevel--
                println("Brightness level decreased to $brightnessLevel.")
            }
            else -> println("Brightness level was not decreased as the smart light device isn't on.")
        }
    }    
    
    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }
    
    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart light turned off.")
    }
}

// The SmartHome class HAS-A smart TV device and smart light.
class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {
    
    // maxValue 2 includes smartTvDevice and smartLightDevice
    var deviceTurnOnCount by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 2)
    
    fun turnOnTv() {
        deviceTurnOnCount++
        smartTvDevice.turnOn()
    }
    
    fun turnOffTv() {
        deviceTurnOnCount--
        smartTvDevice.turnOff()
    }
    
    fun increaseTvVolume() {
        smartTvDevice.increaseSpeakerVolume()
    }
    
    fun decreaseTvVolume() {
        smartTvDevice.decreaseSpeakerVolume()
    }
    
    fun changeTvChannelToNext() {
        smartTvDevice.nextChannel()
    }

    fun changeTvChannelToPrevious() {
        smartTvDevice.previousChannel()
    }
    
    fun turnOnLight() {
        deviceTurnOnCount++
        smartLightDevice.turnOn()
    }
    
    fun turnOffLight() {
        deviceTurnOnCount--
        smartLightDevice.turnOff()
    }
    
    fun increaseLightBrightness() {
        smartLightDevice.increaseBrightness()
    }
    
    fun turnOffAllDevices() {
        turnOffTv()
        turnOffLight()
    }
    
    fun printSmartTvInfo() {
        smartTvDevice.printDeviceInfo()
    }
    
    fun printSmartLightInfo() {
        smartLightDevice.printDeviceInfo()
    }
}
    
class RangeRegulator(
	initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int,
) : ReadWriteProperty<Any?, Int> {
    
    var fieldData = initialValue
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): Int{
    	return fieldData
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int){
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}

fun main() {
    val smartTvDevice: SmartTvDevice = SmartTvDevice(deviceName = "Android TV", deviceCategory = "Entertainment")
    
    val smartLightDevice: SmartLightDevice = SmartLightDevice(deviceName = "Google Light", deviceCategory = "Utility")
    
    val smartHome: SmartHome = SmartHome(smartTvDevice = smartTvDevice, smartLightDevice = smartLightDevice)
    
    println("Smart home has ${smartHome.deviceTurnOnCount} turned-on devices.")
    smartHome.turnOnTv()
    smartHome.printSmartTvInfo()
    smartHome.printSmartLightInfo()
}
