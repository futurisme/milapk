package com.example.model

enum class MilitaryBranch(val title: String, val badge: String, val perkDescription: String) {
    INFANTERI("Infanteri Raider", "⚔️", "Bonus +15% Daya Tahan Pasukan di Medan Berat & Rimba"),
    KAVALERI("Kavaleri Mekanis", "🛡️", "Bonus +20% Mobilitas Kendaraan Tempur & Daya Tembak Armor"),
    PASPAMUS("Pasukan Khusus Siluman", "🦅", "Bonus +25% Tingkat Keberhasilan Penetrasi & Misi Rahasia"),
    INTELIJEN("Intelijen Pertahanan Strategis", "👁️", "Bonus +30% Deteksi Dini Ancaman Geopolitik & Lobi Mabes")
}

data class MilitaryMedal(
    val id: String,
    val name: String,
    val ribbonColorHex: String,
    val description: String,
    val prestigeBonus: Int
)

data class CareerEventLog(
    val year: Int,
    val month: Int,
    val title: String,
    val details: String,
    val isMilestone: Boolean = false
)

data class PlayerProfile(
    val name: String = "Aria Sena",
    val nrp: String = "112000-089", // Nomor Registrasi Prajurit Akmil 2000
    val birthYear: Int = 1978,
    val currentYear: Int = 2000,
    val currentMonth: Int = 1, // 1 to 12
    val rank: MilitaryRank = MilitaryRank.KADET,
    val post: MilitaryPost = MilitaryPostsRegistry.getPostForRank(MilitaryRank.KADET),
    val branch: MilitaryBranch = MilitaryBranch.INFANTERI,
    val monthsInCurrentRank: Int = 0,
    
    // Core Attributes (0 to 100)
    val leadership: Int = 65,
    val tacticalAcumen: Int = 60,
    val discipline: Int = 78,
    val physicalCondition: Int = 85,
    val politicalInfluence: Int = 30,
    val troopsMorale: Int = 75,
    
    // Career Metrics
    val meritPoints: Int = 25,
    val operationalFunds: Long = 150_000_000, // Rp 150 Juta anggaran awal satuan
    val missionsCompleted: Int = 0,
    val tacticalVictories: Int = 0,
    val seskoadPassed: Boolean = false,
    val lemhannasPassed: Boolean = false,
    
    val medals: List<MilitaryMedal> = listOf(
        MilitaryMedal(
            id = "medal_akmil_top",
            name = "Pedang Prasetya Taruna",
            ribbonColorHex = "#D4AF37",
            description = "Dianugerahkan atas kelulusan prima Korps Taruna Akademi Militer tahun 2000.",
            prestigeBonus = 10
        )
    ),
    val brevets: List<String> = listOf("Yudha Wastu Pramuka (Infanteri)"),
    val careerLogs: List<CareerEventLog> = listOf(
        CareerEventLog(
            year = 2000,
            month = 1,
            title = "Awal Pengabdian Militer",
            details = "Memasuki semester akhir Resimen Taruna Akmil Lembah Tidar menjelang Praspa 2000.",
            isMilestone = true
        )
    )
) {
    val age: Int get() = currentYear - birthYear

    val monthName: String
        get() = when (currentMonth) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Bulan $currentMonth"
        }

    fun canBePromoted(): Boolean {
        val next = rank.nextRank() ?: return false
        val yearsOfService = (currentYear - 2000)
        val meetsMerit = meritPoints >= next.minMeritPoints
        val meetsService = yearsOfService >= next.minYearsOfService
        val meetsDiscipline = discipline >= 60

        // Pamen ke Kolonel butuh Seskoad
        if (next == MilitaryRank.KOLONEL && !seskoadPassed) return false
        // Pati Bintang 2 ke atas butuh Lemhannas
        if (next == MilitaryRank.MAYJEN && !lemhannasPassed) return false

        return meetsMerit && meetsService && meetsDiscipline
    }

    fun missingPromotionCriteria(): List<String> {
        val next = rank.nextRank() ?: return emptyList()
        val list = mutableListOf<String>()
        val yearsOfService = (currentYear - 2000)

        if (meritPoints < next.minMeritPoints) {
            list.add("Poin Prestasi: $meritPoints / ${next.minMeritPoints}")
        }
        if (yearsOfService < next.minYearsOfService) {
            list.add("Masa Dinas: $yearsOfService thn / min ${next.minYearsOfService} thn (TMT ${2000 + next.minYearsOfService})")
        }
        if (discipline < 60) {
            list.add("Disiplin Rendah: $discipline / min 60")
        }
        if (next == MilitaryRank.KOLONEL && !seskoadPassed) {
            list.add("Belum Lulus Sekolah Staf dan Komando (SESKOAD)")
        }
        if (next == MilitaryRank.MAYJEN && !lemhannasPassed) {
            list.add("Belum Lulus Kursus Reguler Lemhannas RI")
        }
        return list
    }
}
