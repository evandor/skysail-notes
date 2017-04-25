package io.skysail.core.model

sealed trait ResourceType
case object LIST_RESOURCE extends ResourceType
case object ENTITY_RESOURCE extends ResourceType
case object CREATE_ENTITY_RESOURCE extends ResourceType
case object UPDATE_ENTITY_RESOURCE extends ResourceType
case object UNSPECIFIED_RESOURCE extends ResourceType
