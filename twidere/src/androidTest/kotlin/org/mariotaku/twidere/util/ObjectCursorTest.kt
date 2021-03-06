/*
 *             Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2017 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.util

import android.database.MatrixCursor
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.mariotaku.library.objectcursor.ObjectCursor
import org.mariotaku.twidere.model.FiltersData
import org.mariotaku.twidere.model.ParcelableStatus

/**
 * Created by mariotaku on 2017/3/5.
 */
@RunWith(AndroidJUnit4::class)
class ObjectCursorTest {

    @Test
    fun testSimple() {
        val cur = MatrixCursor(emptyArray(), 0)
        ObjectCursor.indicesFrom(cur, ParcelableStatus::class.java)
    }

    @Test
    fun testMemberClass() {
        val cur = MatrixCursor(emptyArray(), 0)
        ObjectCursor.indicesFrom(cur, FiltersData.BaseItem::class.java)
    }
}
