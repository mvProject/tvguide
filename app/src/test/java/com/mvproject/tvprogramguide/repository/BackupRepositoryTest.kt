package com.mvproject.tvprogramguide.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mvproject.tvprogramguide.data.model.backup.TvBackup
import com.mvproject.tvprogramguide.data.repository.BackupRepository
import com.mvproject.tvprogramguide.domain.contract.IBackupDataSource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.serialization.json.Json

class BackupRepositoryTest : FunSpec({

    lateinit var backupDataSource: IBackupDataSource
    lateinit var auth: FirebaseAuth
    lateinit var repository: BackupRepository

    val testUid = "test-uid"

    beforeTest {
        backupDataSource = mockk()
        auth = mockk()
        repository = BackupRepository(backupDataSource, auth)
    }

    afterTest {
        unmockkAll()
    }

    context("saveBackup") {
        test("calls backupDataSource.saveBackup with correct uid and json when user is logged in") {
            val firebaseUser = mockk<FirebaseUser>()
            every { auth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns testUid

            val tvBackup = TvBackup(timeStamp = 1_000_000L)
            val expectedJson = Json.encodeToString(tvBackup)

            coEvery {
                backupDataSource.saveBackup(
                    uid = testUid,
                    tvBackupJson = expectedJson
                )
            } returns Unit

            repository.saveBackup(tvBackup)

            verify(exactly = 1) { auth.currentUser }
            verify(exactly = 1) { firebaseUser.uid }
            coVerify(exactly = 1) {
                backupDataSource.saveBackup(uid = testUid, tvBackupJson = expectedJson)
            }
            confirmVerified(backupDataSource, auth, firebaseUser)
        }

        test("does NOT call backupDataSource.saveBackup when no user is logged in") {
            every { auth.currentUser } returns null

            repository.saveBackup(TvBackup())

            verify(exactly = 1) { auth.currentUser }
            coVerify(exactly = 0) { backupDataSource.saveBackup(any(), any()) }
            confirmVerified(backupDataSource, auth)
        }

        test("encodes TvBackup with channelsData correctly when user is logged in") {
            val firebaseUser = mockk<FirebaseUser>()
            every { auth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns testUid

            val tvBackup = TvBackup(
                timeStamp = 500L,
                channelsData = listOf(
                    TvBackup.PlaylistBackup(
                        id = 1,
                        name = "My List",
                        isSelected = true,
                        content = listOf("ch1", "ch2")
                    )
                )
            )
            val expectedJson = Json.encodeToString(tvBackup)

            coEvery {
                backupDataSource.saveBackup(
                    uid = testUid,
                    tvBackupJson = expectedJson
                )
            } returns Unit

            repository.saveBackup(tvBackup)

            verify(exactly = 1) { auth.currentUser }
            verify(exactly = 1) { firebaseUser.uid }
            coVerify(exactly = 1) {
                backupDataSource.saveBackup(uid = testUid, tvBackupJson = expectedJson)
            }
            confirmVerified(backupDataSource, auth, firebaseUser)
        }
    }

    context("getBackup") {
        test("returns correct TvBackup when user is logged in and json is valid") {
            val firebaseUser = mockk<FirebaseUser>()
            every { auth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns testUid

            val expected = TvBackup(timeStamp = 1_234_567L)
            val validJson = Json.encodeToString(expected)

            coEvery { backupDataSource.getBackup(uid = testUid) } returns validJson

            val result = repository.getBackup()

            result shouldNotBe null
            result shouldBe expected

            verify(exactly = 1) { auth.currentUser }
            verify(exactly = 1) { firebaseUser.uid }
            coVerify(exactly = 1) { backupDataSource.getBackup(uid = testUid) }
            confirmVerified(backupDataSource, auth, firebaseUser)
        }

        test("returns null when user is logged in but backupDataSource returns null") {
            val firebaseUser = mockk<FirebaseUser>()
            every { auth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns testUid

            coEvery { backupDataSource.getBackup(uid = testUid) } returns null

            val result = repository.getBackup()

            result shouldBe null

            verify(exactly = 1) { auth.currentUser }
            verify(exactly = 1) { firebaseUser.uid }
            coVerify(exactly = 1) { backupDataSource.getBackup(uid = testUid) }
            confirmVerified(backupDataSource, auth, firebaseUser)
        }

        test("returns null when no user is logged in") {
            every { auth.currentUser } returns null

            val result = repository.getBackup()

            result shouldBe null

            verify(exactly = 1) { auth.currentUser }
            coVerify(exactly = 0) { backupDataSource.getBackup(any()) }
            confirmVerified(backupDataSource, auth)
        }

        test("returns null when json is malformed") {
            val firebaseUser = mockk<FirebaseUser>()
            every { auth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns testUid

            coEvery { backupDataSource.getBackup(uid = testUid) } returns "not-valid-json"

            val result = repository.getBackup()

            result shouldBe null

            verify(exactly = 1) { auth.currentUser }
            verify(exactly = 1) { firebaseUser.uid }
            coVerify(exactly = 1) { backupDataSource.getBackup(uid = testUid) }
            confirmVerified(backupDataSource, auth, firebaseUser)
        }

        test("returns TvBackup with all fields populated correctly") {
            val firebaseUser = mockk<FirebaseUser>()
            every { auth.currentUser } returns firebaseUser
            every { firebaseUser.uid } returns testUid

            val expected = TvBackup(
                timeStamp = 9_999_999L,
                theme = 1,
                programsViewCount = 5,
                channelsUpdatePeriod = 14,
                programsUpdatePeriod = 3,
                channelsData = listOf(
                    TvBackup.PlaylistBackup(
                        id = 42,
                        name = "Work",
                        isSelected = false,
                        content = listOf("ch-a", "ch-b", "ch-c")
                    )
                )
            )
            val validJson = Json.encodeToString(expected)

            coEvery { backupDataSource.getBackup(uid = testUid) } returns validJson

            val result = repository.getBackup()

            result shouldBe expected
            verify(exactly = 1) { auth.currentUser }
            verify(exactly = 1) { firebaseUser.uid }
            coVerify(exactly = 1) { backupDataSource.getBackup(uid = testUid) }
            confirmVerified(backupDataSource, auth, firebaseUser)
        }
    }
})
