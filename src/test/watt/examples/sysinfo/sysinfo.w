import 'std.sys'
import 'ext.tui'
import 'syscolors.w'
import 'std.io'
import 'std.convert'
import 'std.jvm'
import 'std.threads'

window := new TuiWindow()
cpu_load := 0
ticks := system.get_hal().getProcessor().getSystemCpuLoadTicks()
threads.run(fun() {
	while true {
		threads.sleep(1000)
		cpu_load = system.get_hal().getProcessor().getSystemCpuLoadBetweenTicks(ticks)
		ticks = system.get_hal().getProcessor().getSystemCpuLoadTicks()
	}
}, [])

fun os_info() -> {
    os := system.get_info().getOperatingSystem()
    window.render_line(
        colors.green + '🎉 System: '
        + os + colors.reset
    )
    window.render_line(
        colors.green + '│->' + colors.lime + ' Uptime: '
        + os.getSystemUptime()/60 + 'mins'
    )
    window.render_line(
        colors.green + '│->' + colors.lime + ' User: '
        + system.get_property('user.name')
    )
    window.render_line(
        colors.green + '│->' + colors.lime + ' Tasks: '
        + os.getProcessCount()
    )
    window.render_line(
        colors.green + '╰->' + colors.lime + ' Arch: '
        + os.getBitness() + '-bit'
    )
}

fun cpu_info() -> {
    window.render_line(
        colors.green + '🎯 Cpu: '
        + system.get_hal().getProcessor().
        getProcessorIdentifier().getName() + colors.reset
    )
    window.render_line(
        colors.green + '│->' + colors.lime + ' CpuPhysicalCores: '
        + system.get_hal().getProcessor().getPhysicalProcessorCount()
    )
    window.render_line(
        colors.green + '│->' + colors.lime + ' CpuLoad: '
        + convert.to_int(cpu_load * 100.0) + ' %'
    )
    window.render_line(
        colors.green + '│->' + colors.lime + ' CpuLogicalCores: '
        + system.get_hal().getProcessor().getLogicalProcessorCount()
    )
    window.render_line(
        colors.green + '╰->' + colors.lime + ' CpuTemp: '
        + convert.to_int(system.get_hal().getSensors().getCpuTemperature()) + '°C'
    )
}


fun gpu_info() -> {
    if system.get_hal().getGraphicsCards().isEmpty() == false {
        card := system.get_hal().getGraphicsCards().get(0)
        window.render_line(
            colors.green + '💡 Gpu: '
            + card.getName() + colors.reset
        )
        window.render_line(
            colors.green + '│-> ' + colors.lime
            + card.getVersionInfo()
        )
        window.render_line(
            colors.green + '╰-> ' + colors.lime + 'VRam: '
            + card.getVRam() / 1024 / 1024 + ' MB'
        )
    } else {
        window.render_line(
            colors.green + '💡 Gpu: no gpu' + colors.reset
        )
    }
}

fun fan_info() -> {
        fans := system.get_hal().getSensors().getFanSpeeds()
        for i in 0 to jvm.array_length(fans) {
                window.render_line(
                    colors.green + '🪭 Fan #' + i + ': ' + colors.lime
                    + jvm.array_element(fans, i) + colors.reset
                )
        }
}

fun ram_info() -> {
    memory := system.get_hal().getMemory()
    window.render_line(
        colors.green + '📥 Ram' + colors.reset
    )
    window.render_line(
        colors.green + '╰-> ' + colors.lime + memory.toString()
    )
}

while true {
    window.render_line(
        colors.green + '...................................'
        + colors.reset
    )
    os_info()
    cpu_info()
    gpu_info()
    ram_info()
    fan_info()
    window.render_line(
        colors.green + '...................................'
        + colors.reset
    )
    threads.sleep(1000)
    window.clear()
}