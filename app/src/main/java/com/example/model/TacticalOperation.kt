package com.example.model

enum class OperationTheater(val label: String, val environment: String, val icon: String) {
    RIMBA_PERBATASAN("Sektor Perbatasan Rimba", "Hutan Tropis Lebat, Kabut Lembah", "🌲"),
    RAWA_PESISIR("Pesisir & Muara Sungai", "Rawa Pasang Surut, Bakau", "🌊"),
    PERKOTAAN_TAKTIKAL("Kompleks Perkotaan Taktis", "Gedung Bertingkat, Barikade", "🏙️"),
    KEPULAUAN_LUAR("Gugus Kepulauan Terluar", "Pendaratan Amfibi, Karang", "🏝️"),
    DATARAN_TINGGI("Pegunungan Sektor Strategis", "Punggung Bukit Curam, Bunker", "⛰️")
}

data class TacticalUnit(
    val id: String,
    val name: String,
    val isFriendly: Boolean,
    var x: Float, // 0.0 to 1.0 grid relative coordinate
    var y: Float, // 0.0 to 1.0 grid relative coordinate
    var health: Int = 100,
    var maxHealth: Int = 100,
    val unitType: String, // "Infanteri", "Sniper", "Lapis Baja", "Komando", "Musuh Gerilya", "Bunker Pertahanan"
    var isSuppressed: Boolean = false
)

data class TacticalActionChoice(
    val id: String,
    val title: String,
    val tacticalCost: String,
    val description: String,
    val successStatRequirement: String // "TACTICS", "LEADERSHIP", "PHYSICAL", "BUDGET"
)

data class TacticalMission(
    val id: String,
    val codeName: String,
    val title: String,
    val yearAvailable: Int,
    val theater: OperationTheater,
    val briefing: String,
    val recommendedRank: MilitaryRank,
    val enemyStrengthDesc: String,
    val meritReward: Int,
    val budgetReward: Long,
    val initialUnits: List<TacticalUnit>
)

data class BattleTurnResult(
    val round: Int,
    val actionTaken: String,
    val narrative: String,
    val friendlyDamageTaken: Int,
    val enemyDamageDealt: Int,
    val tacticalAdvantageGained: Int,
    val isDecisive: Boolean = false
)

object TacticalMissionsRegistry {
    val MISSIONS = listOf(
        TacticalMission(
            id = "mission_2000_frontier",
            codeName = "OPERASI BADAI RIMBA 2000",
            title = "Pembersihan Infiltrasi Patok Perbatasan",
            yearAvailable = 2000,
            theater = OperationTheater.RIMBA_PERBATASAN,
            briefing = "Satelit dan laporan warga mendeteksi pos bayangan bersenjata melintasi patok batas 14. Pimpin peleton bergerak senyap memutus jalur pasokan mereka.",
            recommendedRank = MilitaryRank.LETDA,
            enemyStrengthDesc = "1 Regu Bersenjata Gerilya Lintas Batas",
            meritReward = 45,
            budgetReward = 40_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Peleton 1 Senapan", true, 0.2f, 0.7f, 100, 100, "Infanteri"),
                TacticalUnit("f2", "Regu Senapan Mesin Ringan", true, 0.35f, 0.8f, 100, 100, "Senapan Mesin"),
                TacticalUnit("e1", "Pos Jaga Gerilya", false, 0.7f, 0.3f, 90, 90, "Musuh Gerilya"),
                TacticalUnit("e2", "Sarang Penembak Runduk", false, 0.8f, 0.15f, 75, 75, "Sniper")
            )
        ),
        TacticalMission(
            id = "mission_2002_security",
            codeName = "OPERASI PULIH WIRA 2002",
            title = "Pemulihan Keamanan Distrik Rawan",
            yearAvailable = 2002,
            theater = OperationTheater.DATARAN_TINGGI,
            briefing = "Kelompok separatis bersenjata menduduki stasiun pemancar komunikasi distrik pegunungan. Gelar manuver jepit untuk mengamankan infrastruktur vital.",
            recommendedRank = MilitaryRank.LETTU,
            enemyStrengthDesc = "2 Peleton Musuh Bercokol di Lereng Bukit",
            meritReward = 75,
            budgetReward = 85_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Kompi Raider Tim Alfa", true, 0.25f, 0.65f, 100, 100, "Infanteri"),
                TacticalUnit("f2", "Tim Mortir 60mm", true, 0.15f, 0.85f, 100, 100, "Artileri Ringan"),
                TacticalUnit("e1", "Bunker Pemancar Musuh", false, 0.65f, 0.25f, 120, 120, "Bunker Pertahanan"),
                TacticalUnit("e2", "Patroli Bersenjata Musuh", false, 0.5f, 0.4f, 80, 80, "Musuh Gerilya")
            )
        ),
        TacticalMission(
            id = "mission_2005_disaster_relief",
            codeName = "OPERASI KASIH NUSA 2005",
            title = "Misi Penyelamatan Bencana & Evakuasi Taktis",
            yearAvailable = 2005,
            theater = OperationTheater.RAWA_PESISIR,
            briefing = "Gempa dahsyat dan tsunami melanda garis pantai barat. Laksanakan evakuasi massal heliborne dan pengamanan gudang logistik dari penjarahan anarkis.",
            recommendedRank = MilitaryRank.KAPTEN,
            enemyStrengthDesc = "Medan Ekstrem & Penjarah Logistik Bersenjata",
            meritReward = 90,
            budgetReward = 120_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Satgas Zeni Tempur", true, 0.2f, 0.6f, 100, 100, "Infanteri"),
                TacticalUnit("f2", "Helikopter Puma Evakuasi", true, 0.4f, 0.8f, 100, 100, "Dukungan Udara"),
                TacticalUnit("e1", "Rintangan Reruntuhan & Pasang", false, 0.6f, 0.45f, 100, 100, "Rintangan Alam"),
                TacticalUnit("e2", "Kelompok Penjarah Bersenjata", false, 0.75f, 0.3f, 85, 85, "Musuh Anarkis")
            )
        ),
        TacticalMission(
            id = "mission_2010_counter_terror",
            codeName = "OPERASI PETIR RAIDER 2010",
            title = "Penyergapan Teroris Jaringan Transnasional",
            yearAvailable = 2010,
            theater = OperationTheater.PERKOTAAN_TAKTIKAL,
            briefing = "Intelijen mendeteksi perakitan bahan peledak berdaya ledak tinggi di safe house tersembunyi. Lakukan penyerbuan dinamis CQB (Close Quarters Battle) tanpa korban sipil.",
            recommendedRank = MilitaryRank.MAYOR,
            enemyStrengthDesc = "Sel Teroris Terlatih dengan Jebakan Booby Trap",
            meritReward = 140,
            budgetReward = 200_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Unit Khusus Gultor (Penanggulangan Teror)", true, 0.3f, 0.7f, 100, 100, "Pasukan Khusus"),
                TacticalUnit("f2", "Penembak Jitu Senyap", true, 0.15f, 0.5f, 100, 100, "Sniper"),
                TacticalUnit("e1", "Kubu Pertahanan Safe House", false, 0.7f, 0.35f, 140, 140, "Kubu Bersenjata"),
                TacticalUnit("e2", "Penjaga Keliling dengan Rompi Bom", false, 0.55f, 0.2f, 95, 95, "Milisi Ekstrem")
            )
        ),
        TacticalMission(
            id = "mission_2016_joint_wargame",
            codeName = "LATIHAN GABUNGAN BIMA YUDHA 2016",
            title = "Manuver Skala Brigade Antar-Kecabangan",
            yearAvailable = 2016,
            theater = OperationTheater.KEPULAUAN_LUAR,
            briefing = "Simulasi perang wargame skala penuh. Pimpin brigade mengoordinasikan pendaratan tank amfibi, tabir asap artileri medan, dan serangan kilat merebut pangkalan udara.",
            recommendedRank = MilitaryRank.KOLONEL,
            enemyStrengthDesc = "Divisi Lapis Baja Pasukan Merah (Aggressor)",
            meritReward = 220,
            budgetReward = 450_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Batalyon Mekanis Tank Harimau", true, 0.2f, 0.75f, 120, 120, "Lapis Baja"),
                TacticalUnit("f2", "Baterai Artileri MLRS Astros", true, 0.1f, 0.9f, 100, 100, "Artileri Berat"),
                TacticalUnit("e1", "Formasi Tank Musuh Lapis Baja", false, 0.7f, 0.25f, 160, 160, "Lapis Baja"),
                TacticalUnit("e2", "Baterai Rudal Anti-Tank Musuh", false, 0.8f, 0.45f, 110, 110, "Bunker Pertahanan")
            )
        ),
        TacticalMission(
            id = "mission_2022_hybrid_drone",
            codeName = "OPERASI PERISAI SIBER & DRONE 2022",
            title = "Pertahanan Spektrum Elektronik & Pangkalan Drone",
            yearAvailable = 2022,
            theater = OperationTheater.KEPULAUAN_LUAR,
            briefing = "Musuh asing melancarkan perang hibrida: jamming radar pertahanan udara dan serangan kawanan drone kamikaze. Netralkan jamming dan hancurkan pangkalan peluncur.",
            recommendedRank = MilitaryRank.BRIGJEN,
            enemyStrengthDesc = "Satuan Perang Elektronika Canggih & Drone Swarm",
            meritReward = 350,
            budgetReward = 800_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Batalyon Pertahanan Udara & Anti-Drone", true, 0.25f, 0.8f, 130, 130, "Anti-Drone"),
                TacticalUnit("f2", "Satuan Jamming Elektronik Elang", true, 0.15f, 0.6f, 100, 100, "Perang Elektronika"),
                TacticalUnit("e1", "Peluncur Kawanan Drone Musuh", false, 0.8f, 0.2f, 180, 180, "Sarang Drone"),
                TacticalUnit("e2", "Stasiun Radar Gelombang Mikro Musuh", false, 0.65f, 0.35f, 150, 150, "Pusat Radar Musuh")
            )
        ),
        TacticalMission(
            id = "mission_2026_supreme_defense",
            codeName = "OPERASI SENGKETA SAMUDRA 2026",
            title = "Perang Pertahanan Total Kedaulatan Nasional",
            yearAvailable = 2026,
            theater = OperationTheater.KEPULAUAN_LUAR,
            briefing = "Armada koalisi asing mencoba memblokade selat strategis dan menduduki kepulauan terluar Republik. Sebagai Panglima Tertinggi, kerahkan seluruh matra untuk mempertahankan tanah air!",
            recommendedRank = MilitaryRank.JENDERAL,
            enemyStrengthDesc = "Armada Tempur Gabungan Asing Bersenjata Rudal Hipersonik",
            meritReward = 800,
            budgetReward = 2_500_000_000,
            initialUnits = listOf(
                TacticalUnit("f1", "Gugus Tempur Laut & Pertahanan Pantai", true, 0.2f, 0.65f, 200, 200, "Armada Laut"),
                TacticalUnit("f2", "Skuadron Jet Tempur & Rudal Pesisir", true, 0.35f, 0.85f, 180, 180, "Dukungan Udara"),
                TacticalUnit("e1", "Kapal Induk Serbu Asing & Destroyer", false, 0.75f, 0.25f, 280, 280, "Armada Utama Musuh"),
                TacticalUnit("e2", "Gugus Pendarat Amfibi Marinir Musuh", false, 0.6f, 0.15f, 220, 220, "Pasukan Invasi")
            )
        )
    )

    fun getAvailableMissions(year: Int): List<TacticalMission> {
        return MISSIONS.filter { it.yearAvailable <= year }
    }
}
