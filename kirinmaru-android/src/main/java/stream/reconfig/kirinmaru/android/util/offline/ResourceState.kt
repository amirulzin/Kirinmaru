package stream.reconfig.kirinmaru.android.util.offline

/**
 * Data class for State updates
 */
data class ResourceState(val state: State, val message: String = "", val type: ResourceType = ResourceType.ANY)