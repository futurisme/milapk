package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MilitaryGameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepository(application)

    private val _profile = MutableStateFlow(repository.loadProfile())
    val profile: StateFlow<PlayerProfile> = _profile.asStateFlow()

    private val _activeGeopoliticalEvent = MutableStateFlow<GeopoliticalEvent?>(null)
    val activeGeopoliticalEvent: StateFlow<GeopoliticalEvent?> = _activeGeopoliticalEvent.asStateFlow()

    private val _eventOutcomeMessage = MutableStateFlow<String?>(null)
    val eventOutcomeMessage: StateFlow<String?> = _eventOutcomeMessage.asStateFlow()

    // Tactical Wargame State
    private val _activeMission = MutableStateFlow<TacticalMission?>(null)
    val activeMission: StateFlow<TacticalMission?> = _activeMission.asStateFlow()

    private val _tacticalUnits = MutableStateFlow<List<TacticalUnit>>(emptyList())
    val tacticalUnits: StateFlow<List<TacticalUnit>> = _tacticalUnits.asStateFlow()

    private val _battleTurnLogs = MutableStateFlow<List<BattleTurnResult>>(emptyList())
    val battleTurnLogs: StateFlow<List<BattleTurnResult>> = _battleTurnLogs.asStateFlow()

    private val _missionOutcome = MutableStateFlow<String?>(null) // "VICTORY", "DEFEAT", or null
    val missionOutcome: StateFlow<String?> = _missionOutcome.asStateFlow()

    private val _arsenal = MutableStateFlow(ArsenalAndDoctrineRegistry.INITIAL_ARSENAL)
    val arsenal: StateFlow<List<MilitaryArsenalItem>> = _arsenal.asStateFlow()

    private val _doctrines = MutableStateFlow(ArsenalAndDoctrineRegistry.DOCTRINES)
    val doctrines: StateFlow<List<MilitaryDoctrine>> = _doctrines.asStateFlow()

    private val _promotionNotice = MutableStateFlow<String?>(null)
    val promotionNotice: StateFlow<String?> = _promotionNotice.asStateFlow()

    init {
        // Check for event at initial launch if matching
        checkForEventAtDate(_profile.value.currentYear, _profile.value.currentMonth)
    }

    private fun save() {
        repository.saveProfile(_profile.value)
    }

    private fun checkForEventAtDate(year: Int, month: Int) {
        val event = GeopoliticalTimelineRegistry.getEventForYearAndMonth(year, month)
        if (event != null) {
            _activeGeopoliticalEvent.value = event
        }
    }

    /**
     * Maju satu bulan dalam simulasi militer 2000 - 2026+.
     */
    fun advanceMonth() {
        val current = _profile.value
        var nextMonth = current.currentMonth + 1
        var nextYear = current.currentYear
        if (nextMonth > 12) {
            nextMonth = 1
            nextYear += 1
        }

        // Gaji dan tunjangan operasional bulanan
        val monthlyBudgetGain = (current.rank.baseMonthlySalary * 1.5).toLong() +
                (current.post.operationalBudgetMultiplier * 10_000_000L).toLong()

        // Peningkatan masa dinas dalam pangkat
        val nextMonthsInRank = current.monthsInCurrentRank + 1

        // Kejadian acak dinas bulanan (patroli, latihan rutin, kepatuhan)
        val meritGain = Random.nextInt(3, 8)
        val moraleShift = Random.nextInt(-2, 4)
        val disciplineShift = Random.nextInt(0, 2)

        val updated = current.copy(
            currentYear = nextYear,
            currentMonth = nextMonth,
            monthsInCurrentRank = nextMonthsInRank,
            operationalFunds = (current.operationalFunds + monthlyBudgetGain).coerceAtLeast(0L),
            meritPoints = current.meritPoints + meritGain,
            troopsMorale = (current.troopsMorale + moraleShift).coerceIn(30, 100),
            discipline = (current.discipline + disciplineShift).coerceIn(30, 100)
        )

        _profile.value = updated
        save()

        // Check timeline event
        checkForEventAtDate(nextYear, nextMonth)

        // Check if eligible for promotion notice
        if (updated.canBePromoted() && _promotionNotice.value == null) {
            val nextRank = updated.rank.nextRank()
            if (nextRank != null) {
                _promotionNotice.value = "Sidang Wanjakti siap digelar untuk kenaikan pangkat menjadi ${nextRank.title} (${nextRank.abbreviation})!"
            }
        }
    }

    fun dismissPromotionNotice() {
        _promotionNotice.value = null
    }

    /**
     * Memproses kenaikan pangkat resmi melalui Sidang Wanjakti
     */
    fun promoteOfficer(): Boolean {
        val current = _profile.value
        val nextRank = current.rank.nextRank() ?: return false
        if (!current.canBePromoted()) return false

        val newPost = MilitaryPostsRegistry.getPostForRank(nextRank)
        val newLog = CareerEventLog(
            year = current.currentYear,
            month = current.currentMonth,
            title = "Kenaikan Pangkat: ${nextRank.title}",
            details = "Resmi dilantik naik pangkat menjadi ${nextRank.title} (${nextRank.abbreviation}) dan mengemban jabatan ${newPost.name}.",
            isMilestone = true
        )

        val updatedLogs = current.careerLogs + newLog
        val updatedMedals = current.medals.toMutableList()

        // Anugerah tanda jasa sesuai pangkat
        when (nextRank) {
            MilitaryRank.KAPTEN -> {
                updatedMedals.add(
                    MilitaryMedal("medal_8_years", "Satyalancana Kesetiaan VIII Tahun", "#39E75F", "Tanda kesetiaan 8 tahun tanpa cacat.", 15)
                )
            }
            MilitaryRank.KOLONEL -> {
                updatedMedals.add(
                    MilitaryMedal("medal_16_years", "Satyalancana Kesetiaan XVI Tahun", "#FF9E00", "Dedikasi 16 tahun memimpin formasi tempur.", 25)
                )
            }
            MilitaryRank.MAYJEN -> {
                updatedMedals.add(
                    MilitaryMedal("medal_kartika", "Bintang Kartika Eka Paksi Pratama", "#D4AF37", "Jasa luar biasa dalam memajukan pertahanan matra darat.", 40)
                )
            }
            MilitaryRank.JENDERAL -> {
                updatedMedals.add(
                    MilitaryMedal("medal_yudha", "Bintang Yudha Dharma Utama", "#00E5FF", "Kepemimpinan tertinggi gabungan seluruh matra pertahanan.", 60)
                )
            }
            MilitaryRank.JENDERAL_BESAR -> {
                updatedMedals.add(
                    MilitaryMedal("medal_mahaputera", "Bintang Republik Indonesia Adipurna", "#FFD700", "Pahlawan Tertinggi Kedaulatan Bangsa.", 100)
                )
            }
            else -> {}
        }

        _profile.value = current.copy(
            rank = nextRank,
            post = newPost,
            monthsInCurrentRank = 0,
            leadership = (current.leadership + 8).coerceAtMost(100),
            politicalInfluence = (current.politicalInfluence + 10).coerceAtMost(100),
            meritPoints = current.meritPoints + 50,
            medals = updatedMedals,
            careerLogs = updatedLogs
        )
        save()
        _promotionNotice.value = null
        return true
    }

    /**
     * Kegiatan Dinas Perwira (Latihan, Inspeksi, Kursus Komando, Lobi)
     */
    fun executeOfficerDuty(dutyType: String): String {
        val current = _profile.value
        val cost: Long
        val logTitle: String
        val logDesc: String

        val updated = when (dutyType) {
            "LATIHAN_TEMPUR" -> {
                cost = 25_000_000L
                if (current.operationalFunds < cost) return "Anggaran operasional tidak mencukupi untuk latihan amunisi tajam!"
                logTitle = "Latihan Tembak & Taktik Pasukan"
                logDesc = "Menggelar latihan taktik regu dan tembakan presisi di daerah latihan."
                current.copy(
                    operationalFunds = current.operationalFunds - cost,
                    tacticalAcumen = (current.tacticalAcumen + 4).coerceAtMost(100),
                    discipline = (current.discipline + 3).coerceAtMost(100),
                    meritPoints = current.meritPoints + 15
                )
            }
            "INSPEKSI_MORAL" -> {
                cost = 10_000_000L
                if (current.operationalFunds < cost) return "Anggaran tidak mencukupi untuk perbaikan barak dan logistik!"
                logTitle = "Inspeksi Barak & Kesejahteraan Prajurit"
                logDesc = "Mengecek kesiapan peralatan dan meningkatkan gizi ransum tempur prajurit."
                current.copy(
                    operationalFunds = current.operationalFunds - cost,
                    troopsMorale = (current.troopsMorale + 8).coerceAtMost(100),
                    leadership = (current.leadership + 4).coerceAtMost(100),
                    meritPoints = current.meritPoints + 10
                )
            }
            "PENDIDIKAN_SESKOAD" -> {
                cost = 50_000_000L
                if (current.seskoadPassed) return "Anda sudah lulus Seskoad!"
                if (current.rank.ordinal < MilitaryRank.MAYOR.ordinal) return "Seskoad hanya untuk perwira berpangkat Mayor ke atas!"
                if (current.operationalFunds < cost) return "Biaya diklat Seskoad belum tercukupi."
                logTitle = "Kelulusan SESKOAD"
                logDesc = "Lulus Sekolah Staf dan Komando Angkatan Darat dengan predikat Wira Wisesa."
                current.copy(
                    operationalFunds = current.operationalFunds - cost,
                    seskoadPassed = true,
                    tacticalAcumen = (current.tacticalAcumen + 10).coerceAtMost(100),
                    leadership = (current.leadership + 8).coerceAtMost(100),
                    meritPoints = current.meritPoints + 80,
                    brevets = current.brevets + "Staf & Komando (Seskoad)"
                )
            }
            "PENDIDIKAN_LEMHANNAS" -> {
                cost = 100_000_000L
                if (current.lemhannasPassed) return "Anda sudah lulus Lemhannas!"
                if (current.rank.ordinal < MilitaryRank.BRIGJEN.ordinal) return "Lemhannas hanya untuk Perwira Tinggi / Brigjen ke atas!"
                if (current.operationalFunds < cost) return "Biaya penelitian strategis Lemhannas belum tercukupi."
                logTitle = "Kelulusan Lemhannas RI"
                logDesc = "Lulus Kursus Reguler Lemhannas dalam Geopolitik & Ketahanan Nasional."
                current.copy(
                    operationalFunds = current.operationalFunds - cost,
                    lemhannasPassed = true,
                    politicalInfluence = (current.politicalInfluence + 20).coerceAtMost(100),
                    leadership = (current.leadership + 12).coerceAtMost(100),
                    meritPoints = current.meritPoints + 150,
                    brevets = current.brevets + "Ketahanan Nasional (Lemhannas)"
                )
            }
            "KONSULTASI_MABES" -> {
                cost = 15_000_000L
                logTitle = "Konsultasi Strategis Mabes TNI"
                logDesc = "Menghadiri rapat koordinasi pimpinan untuk menyelaraskan kebijakan pertahanan."
                current.copy(
                    operationalFunds = (current.operationalFunds - cost).coerceAtLeast(0L),
                    politicalInfluence = (current.politicalInfluence + 5).coerceAtMost(100),
                    leadership = (current.leadership + 2).coerceAtMost(100),
                    meritPoints = current.meritPoints + 12
                )
            }
            "PATROLI_BATAS" -> {
                cost = 8_000_000L
                logTitle = "Patroli Pengecekan Patok Batas"
                logDesc = "Menjelajahi jalur perbatasan darat dan perairan terluar bersama regu siaga."
                current.copy(
                    operationalFunds = (current.operationalFunds - cost).coerceAtLeast(0L),
                    physicalCondition = (current.physicalCondition + 4).coerceAtMost(100),
                    discipline = (current.discipline + 3).coerceAtMost(100),
                    meritPoints = current.meritPoints + 15
                )
            }
            else -> return "Tindakan tidak dikenal."
        }

        val updatedLogs = updated.careerLogs + CareerEventLog(
            year = current.currentYear,
            month = current.currentMonth,
            title = logTitle,
            details = logDesc
        )
        _profile.value = updated.copy(careerLogs = updatedLogs)
        save()
        return "$logTitle berhasil dilaksanakan!"
    }

    /**
     * Memilih opsi dalam peristiwa bersejarah/krisis geopolitik
     */
    fun selectEventOption(option: EventOption) {
        val current = _profile.value
        val updated = current.copy(
            leadership = (current.leadership + option.leadershipDelta).coerceIn(0, 100),
            tacticalAcumen = (current.tacticalAcumen + option.tacticalDelta).coerceIn(0, 100),
            discipline = (current.discipline + option.disciplineDelta).coerceIn(0, 100),
            physicalCondition = (current.physicalCondition + option.physicalDelta).coerceIn(0, 100),
            politicalInfluence = (current.politicalInfluence + option.influenceDelta).coerceIn(0, 100),
            troopsMorale = (current.troopsMorale + option.moraleDelta).coerceIn(0, 100),
            meritPoints = current.meritPoints + option.meritDelta,
            operationalFunds = (current.operationalFunds + option.budgetDelta).coerceAtLeast(0L),
            careerLogs = current.careerLogs + CareerEventLog(
                year = current.currentYear,
                month = current.currentMonth,
                title = "Keputusan: ${option.title}",
                details = option.resultNarrative,
                isMilestone = true
            )
        )

        _profile.value = updated
        _eventOutcomeMessage.value = option.resultNarrative
        _activeGeopoliticalEvent.value = null
        save()
    }

    fun dismissEventOutcome() {
        _eventOutcomeMessage.value = null
    }

    /**
     * Memulai Operasi Tempur Taktis / Wargame 2D
     */
    fun startMission(mission: TacticalMission) {
        _activeMission.value = mission
        _tacticalUnits.value = mission.initialUnits.map { it.copy() }
        _battleTurnLogs.value = listOf(
            BattleTurnResult(
                round = 0,
                actionTaken = "PENGGELARAN PASUKAN",
                narrative = "Pasukan telah disiagakan di garis awal operasi. Kode: ${mission.codeName}.",
                friendlyDamageTaken = 0,
                enemyDamageDealt = 0,
                tacticalAdvantageGained = 10
            )
        )
        _missionOutcome.value = null
    }

    /**
     * Eksekusi Perintah Taktis di Ruang Komando 2D
     */
    fun executeTacticalAction(actionType: String) {
        val mission = _activeMission.value ?: return
        if (_missionOutcome.value != null) return

        val units = _tacticalUnits.value.toMutableList()
        val friendlyUnits = units.filter { it.isFriendly && it.health > 0 }
        val enemyUnits = units.filter { !it.isFriendly && it.health > 0 }

        if (friendlyUnits.isEmpty() || enemyUnits.isEmpty()) return

        val currentProfile = _profile.value
        val round = _battleTurnLogs.value.size

        var enemyDamage = 0
        var friendlyDamage = 0
        val actionTitle: String
        val narrative: String

        when (actionType) {
            "MANUVER_FLANK" -> {
                actionTitle = "Manuver Sayap (Flanking Attack)"
                val success = Random.nextInt(100) < (currentProfile.tacticalAcumen + 20)
                if (success) {
                    enemyDamage = Random.nextInt(35, 60)
                    friendlyDamage = Random.nextInt(5, 15)
                    narrative = "Regu pemukul berhasil menyusup dari lambung kiri musuh! Garis pertahanan lawan panik dan porak-poranda."
                    // Pergerakan koordinat unit 2D
                    friendlyUnits.firstOrNull()?.let {
                        it.x = (it.x + 0.15f).coerceAtMost(0.6f)
                        it.y = (it.y - 0.15f).coerceAtLeast(0.3f)
                    }
                } else {
                    enemyDamage = Random.nextInt(15, 25)
                    friendlyDamage = Random.nextInt(15, 30)
                    narrative = "Manuver terdeteksi pos tinjau musuh! Terjadi kontak tembak sengit di lereng terbuka."
                }
            }
            "TEMBAKAN_TABIR" -> {
                actionTitle = "Tembakan Tabir Artileri & Mortir"
                enemyDamage = Random.nextInt(40, 75)
                friendlyDamage = Random.nextInt(2, 10)
                narrative = "Dentuman mortir dan artileri menghujani posisi bunker musuh. Asap tebal mengacaukan koordinasi lawan."
                enemyUnits.forEach { it.isSuppressed = true }
            }
            "SERANGAN_MALAM" -> {
                actionTitle = "Penyusupan Raider Senyap"
                val success = Random.nextInt(100) < (currentProfile.discipline + currentProfile.tacticalAcumen) / 2 + 10
                if (success) {
                    enemyDamage = Random.nextInt(45, 80)
                    friendlyDamage = Random.nextInt(0, 10)
                    narrative = "Pasukan Raider bergerak laksana hantu malam. Titik kunci musuh dilumpuhkan dengan senyap tanpa perlawanan berarti."
                } else {
                    enemyDamage = Random.nextInt(20, 35)
                    friendlyDamage = Random.nextInt(20, 35)
                    narrative = "Ranjau perimeter musuh meletus memicu baku tembak jarak dekat!"
                }
            }
            "BANTUAN_UDARA_CAS" -> {
                actionTitle = "Serangan Udara / Drone Terkoordinasi"
                enemyDamage = Random.nextInt(60, 95)
                friendlyDamage = Random.nextInt(0, 5)
                narrative = "Rudal presisi udara menghantam target sentral musuh dengan akurasi meteran!"
            }
            else -> {
                actionTitle = "Pertahanan Parit Berlapis"
                enemyDamage = Random.nextInt(20, 40)
                friendlyDamage = Random.nextInt(5, 12)
                narrative = "Pasukan bertahan kokoh di balik tanggul dan parit, memukul mundur serbuan lawan dengan disiplin baja."
            }
        }

        // Apply damage to primary enemy
        val targetEnemy = enemyUnits.first()
        targetEnemy.health = (targetEnemy.health - enemyDamage).coerceAtLeast(0)

        // Apply damage to friendly
        val targetFriendly = friendlyUnits.first()
        targetFriendly.health = (targetFriendly.health - friendlyDamage).coerceAtLeast(0)

        val log = BattleTurnResult(
            round = round,
            actionTaken = actionTitle,
            narrative = narrative,
            friendlyDamageTaken = friendlyDamage,
            enemyDamageDealt = enemyDamage,
            tacticalAdvantageGained = if (enemyDamage > friendlyDamage) 15 else -5
        )

        _tacticalUnits.value = units
        _battleTurnLogs.value = _battleTurnLogs.value + log

        // Check victory or defeat
        val remainingEnemies = units.filter { !it.isFriendly && it.health > 0 }
        val remainingFriendlies = units.filter { it.isFriendly && it.health > 0 }

        if (remainingEnemies.isEmpty()) {
            _missionOutcome.value = "VICTORY"
            val updated = currentProfile.copy(
                meritPoints = currentProfile.meritPoints + mission.meritReward,
                operationalFunds = currentProfile.operationalFunds + mission.budgetReward,
                missionsCompleted = currentProfile.missionsCompleted + 1,
                tacticalVictories = currentProfile.tacticalVictories + 1,
                tacticalAcumen = (currentProfile.tacticalAcumen + 3).coerceAtMost(100),
                leadership = (currentProfile.leadership + 2).coerceAtMost(100),
                careerLogs = currentProfile.careerLogs + CareerEventLog(
                    year = currentProfile.currentYear,
                    month = currentProfile.currentMonth,
                    title = "Kemenangan Operasi: ${mission.codeName}",
                    details = "Berhasil menyelesaikan misi ${mission.title}. Memperoleh tanda jasa dan kredit taktis Mabes.",
                    isMilestone = true
                )
            )
            _profile.value = updated
            save()
        } else if (remainingFriendlies.isEmpty()) {
            _missionOutcome.value = "DEFEAT"
            val updated = currentProfile.copy(
                troopsMorale = (currentProfile.troopsMorale - 15).coerceAtLeast(20),
                careerLogs = currentProfile.careerLogs + CareerEventLog(
                    year = currentProfile.currentYear,
                    month = currentProfile.currentMonth,
                    title = "Kemunduran Operasi: ${mission.codeName}",
                    details = "Pasukan terpaksa ditarik mundur untuk konsolidasi akibat tekanan musuh.",
                    isMilestone = false
                )
            )
            _profile.value = updated
            save()
        }
    }

    fun closeMission() {
        _activeMission.value = null
        _missionOutcome.value = null
        _battleTurnLogs.value = emptyList()
        _tacticalUnits.value = emptyList()
    }

    /**
     * Modernisasi Alutsista Satuan
     */
    fun acquireArsenal(item: MilitaryArsenalItem): Boolean {
        val current = _profile.value
        if (current.operationalFunds < item.cost) return false
        if (item.isAcquired) return false

        val updatedArsenal = _arsenal.value.map {
            if (it.id == item.id) it.copy(isAcquired = true) else it
        }
        _arsenal.value = updatedArsenal

        val updatedProfile = current.copy(
            operationalFunds = current.operationalFunds - item.cost,
            tacticalAcumen = (current.tacticalAcumen + item.tacticalBonus).coerceAtMost(100),
            careerLogs = current.careerLogs + CareerEventLog(
                year = current.currentYear,
                month = current.currentMonth,
                title = "Pengadaan Alutsista: ${item.name}",
                details = "Satuan resmi menerima alutsista ${item.name} untuk memperkuat daya pukul operasi."
            )
        )
        _profile.value = updatedProfile
        save()
        return true
    }

    /**
     * Mengadopsi Doktrin Militer Baru
     */
    fun adoptDoctrine(doctrine: MilitaryDoctrine): Boolean {
        val current = _profile.value
        if (current.meritPoints < doctrine.meritRequired) return false
        if (doctrine.isAdopted) return false

        val updatedDoctrines = _doctrines.value.map {
            if (it.id == doctrine.id) it.copy(isAdopted = true) else it
        }
        _doctrines.value = updatedDoctrines

        val updatedProfile = current.copy(
            leadership = (current.leadership + doctrine.leadershipBonus).coerceAtMost(100),
            tacticalAcumen = (current.tacticalAcumen + doctrine.tacticalBonus).coerceAtMost(100),
            careerLogs = current.careerLogs + CareerEventLog(
                year = current.currentYear,
                month = current.currentMonth,
                title = "Penerapan Doktrin: ${doctrine.name}",
                details = "Resmi memberlakukan doktrin tempur ${doctrine.name} sebagai acuan taktis kesatuan."
            )
        )
        _profile.value = updatedProfile
        save()
        return true
    }

    fun resetGame() {
        _profile.value = repository.resetGame()
        _arsenal.value = ArsenalAndDoctrineRegistry.INITIAL_ARSENAL
        _doctrines.value = ArsenalAndDoctrineRegistry.DOCTRINES
        _activeMission.value = null
        _missionOutcome.value = null
        _activeGeopoliticalEvent.value = null
        _eventOutcomeMessage.value = null
    }
}
