package org.zoo;

sealed interface Fish extends Animals permits Lachs, Forelle{
}
