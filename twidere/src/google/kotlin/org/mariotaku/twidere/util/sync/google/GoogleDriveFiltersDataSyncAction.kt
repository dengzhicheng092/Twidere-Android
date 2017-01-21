package org.mariotaku.twidere.util.sync.google

import android.content.Context
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import org.mariotaku.twidere.extension.model.initFields
import org.mariotaku.twidere.extension.model.parse
import org.mariotaku.twidere.extension.model.serialize
import org.mariotaku.twidere.extension.newPullParser
import org.mariotaku.twidere.extension.newSerializer
import org.mariotaku.twidere.model.FiltersData
import org.mariotaku.twidere.util.io.DirectByteArrayOutputStream
import org.mariotaku.twidere.util.sync.FileBasedFiltersDataSyncAction
import java.io.FileNotFoundException
import java.io.InputStream

internal class GoogleDriveFiltersDataSyncAction(
        context: Context,
        val drive: Drive
) : FileBasedFiltersDataSyncAction<CloseableAny<File>, GoogleDriveUploadSession<FiltersData>>(context) {

    private val fileName = "filters.xml"

    private lateinit var commonFolderId: String
    private val files = drive.files()

    override fun newLoadFromRemoteSession(): CloseableAny<File> {
        val file = files.getOrNull(fileName, xmlMimeType, commonFolderId) ?: throw FileNotFoundException()
        return CloseableAny(file)
    }

    override fun CloseableAny<File>.getRemoteLastModified(): Long {
        return (obj.modifiedTime ?: obj.createdTime)?.value ?: 0
    }

    override fun CloseableAny<File>.loadFromRemote(): FiltersData {
        val data = FiltersData()
        data.parse(files.get(obj.id).executeAsInputStream().newPullParser(charset = Charsets.UTF_8))
        data.initFields()
        return data
    }

    override fun GoogleDriveUploadSession<FiltersData>.setRemoteLastModified(lastModified: Long) {
        this.localModifiedTime = lastModified
    }

    override fun GoogleDriveUploadSession<FiltersData>.saveToRemote(data: FiltersData): Boolean {
        return this.uploadData(data)
    }

    override fun newSaveToRemoteSession(): GoogleDriveUploadSession<FiltersData> {
        return object : GoogleDriveUploadSession<FiltersData>(fileName, commonFolderId, xmlMimeType, files) {
            override fun FiltersData.toInputStream(): InputStream {
                val os = DirectByteArrayOutputStream()
                this.serialize(os.newSerializer(charset = Charsets.UTF_8, indent = true))
                return os.inputStream(true)
            }
        }
    }


    override fun setup(): Boolean {
        commonFolderId = files.getOrCreate("Common", folderMimeType).id
        return true
    }

}