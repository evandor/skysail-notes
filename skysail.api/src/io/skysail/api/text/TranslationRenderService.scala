package io.skysail.api.text

import org.osgi.annotation.versioning.ProviderType;

/**
 * A Translation render service asks a TranslationStore for the translation
 * object for a given key and passes this to its render function to create the
 * translated value.
 *
 * Different implementations can post-process the retrieved translation, for
 * example they can provide asciidoc or markdown formatting.
 *
 */
@ProviderType
trait TranslationRenderService {

    def render( content: String,substitutions: Any*): String

    def render( translation: Translation): String

    def applicable( unformattedTranslation: String): Boolean

    def adjustText( unformatted: String): String

    def addRendererInfo(): String

}
