package com.example.model

/**
 * Jabatan dan Posisi Komando Militer Realistis yang sesuai dengan Pangkat.
 */
data class MilitaryPost(
    val id: String,
    val name: String,
    val requiredRank: MilitaryRank,
    val unitCategory: String, // "Akademi", "Batalyon Garis Depan", "Staf Teritorial", "Mabes", "Komando Tertinggi"
    val troopsUnderCommand: Int,
    val jurisdiction: String,
    val operationalBudgetMultiplier: Double,
    val influenceBonus: Int,
    val tacticalBonus: Int,
    val description: String
)

object MilitaryPostsRegistry {
    val ALL_POSTS = listOf(
        // Kadet
        MilitaryPost(
            id = "cadet_regiment_commander",
            name = "Komandan Resimen Korps Taruna",
            requiredRank = MilitaryRank.KADET,
            unitCategory = "Akademi Militer",
            troopsUnderCommand = 450,
            jurisdiction = "Resimen Taruna Lembah Tidar",
            operationalBudgetMultiplier = 1.0,
            influenceBonus = 2,
            tacticalBonus = 3,
            description = "Memimpin rekan kadet dalam latihan baris-berbaris, taktik regu, dan ujian lapangan tahun 2000."
        ),
        // Letda
        MilitaryPost(
            id = "platoon_commander_frontier",
            name = "Komandan Peleton (Danton) 1 Senapan",
            requiredRank = MilitaryRank.LETDA,
            unitCategory = "Batalyon Infanteri 312 Perbatasan",
            troopsUnderCommand = 36,
            jurisdiction = "Pos Perbatasan Utara Sektor Alpha",
            operationalBudgetMultiplier = 1.2,
            influenceBonus = 3,
            tacticalBonus = 5,
            description = "Memimpin patroli patok perbatasan rimba, pengintaian jejak, dan penjagaan kedaulatan langsung di garis depan."
        ),
        // Lettu
        MilitaryPost(
            id = "deputy_company_commander",
            name = "Wakil Komandan Kompi (Wadanki) Raider",
            requiredRank = MilitaryRank.LETTU,
            unitCategory = "Batalyon Raider Reaksi Cepat",
            troopsUnderCommand = 110,
            jurisdiction = "Wilayah Operasi Taktis Cepat",
            operationalBudgetMultiplier = 1.5,
            influenceBonus = 5,
            tacticalBonus = 8,
            description = "Mengkoordinasi kesiapan amunisi kompi, penyergapan taktis malam hari, dan infiltrasi cepat heliborne."
        ),
        // Kapten
        MilitaryPost(
            id = "company_commander_combat",
            name = "Komandan Kompi (Danki) Senapan Tempur",
            requiredRank = MilitaryRank.KAPTEN,
            unitCategory = "Batalyon Mekanis Garda Republik",
            troopsUnderCommand = 145,
            jurisdiction = "Garis Pertahanan Taktis Distrik",
            operationalBudgetMultiplier = 2.0,
            influenceBonus = 8,
            tacticalBonus = 12,
            description = "Komando penuh 145 prajurit dengan dukungan kendaraan tempur lapis baja roda ban dan mortir kompi."
        ),
        // Mayor
        MilitaryPost(
            id = "battalion_exec_officer",
            name = "Wakil Komandan Batalyon (Wadanyon)",
            requiredRank = MilitaryRank.MAYOR,
            unitCategory = "Batalyon Infanteri Lintas Udara",
            troopsUnderCommand = 650,
            jurisdiction = "Komando Markas Batalyon",
            operationalBudgetMultiplier = 2.8,
            influenceBonus = 12,
            tacticalBonus = 15,
            description = "Kepala staf operasional batalyon, memastikan logistik, pemeliharaan senjata berat, dan moral prajurit prima."
        ),
        // Letkol
        MilitaryPost(
            id = "battalion_commander_raider",
            name = "Komandan Batalyon (Danyon) Raider 400",
            requiredRank = MilitaryRank.LETKOL,
            unitCategory = "Batalyon Pasukan Pemukul Utama",
            troopsUnderCommand = 820,
            jurisdiction = "Batalyon Pemukul Cepat Wilayah",
            operationalBudgetMultiplier = 4.0,
            influenceBonus = 18,
            tacticalBonus = 20,
            description = "Pegang tongkat komando Batalyon Raider elite. Menerima misi langsung dari Pangdam untuk penanggulangan krisis."
        ),
        // Kolonel
        MilitaryPost(
            id = "brigade_commander",
            name = "Komandan Brigade Infanteri (Danbrigif)",
            requiredRank = MilitaryRank.KOLONEL,
            unitCategory = "Brigade Tempur Tiga Batalyon",
            troopsUnderCommand = 2800,
            jurisdiction = "Sektor Pertahanan Korps Wilayah",
            operationalBudgetMultiplier = 6.5,
            influenceBonus = 25,
            tacticalBonus = 28,
            description = "Memimpin multi-batalyon infanteri, kavaleri kavaleri lapis baja, dan baterai artileri medan penangkal agresi."
        ),
        // Brigjen
        MilitaryPost(
            id = "chief_of_staff_military_command",
            name = "Kepala Staf Kodam (Kasdam)",
            requiredRank = MilitaryRank.BRIGJEN,
            unitCategory = "Komando Daerah Militer",
            troopsUnderCommand = 12000,
            jurisdiction = "Seluruh Provinsi Strategis Tengah",
            operationalBudgetMultiplier = 10.0,
            influenceBonus = 35,
            tacticalBonus = 34,
            description = "Jenderal bintang satu pengatur operasional internal Komando Daerah Militer, alokasi radar, dan intelijen strategis."
        ),
        // Mayjen
        MilitaryPost(
            id = "regional_military_commander",
            name = "Panglima Kodam (Pangdam)",
            requiredRank = MilitaryRank.MAYJEN,
            unitCategory = "Markas Kodam Utama",
            troopsUnderCommand = 35000,
            jurisdiction = "Komando Teritorial Tiga Provinsi",
            operationalBudgetMultiplier = 16.0,
            influenceBonus = 50,
            tacticalBonus = 42,
            description = "Panglima bintang dua pemegang otoritas pertahanan darat regional. Berwenang menggelar kekuatan militer skala penuh."
        ),
        // Letjen
        MilitaryPost(
            id = "strategic_reserve_commander",
            name = "Panglima Kostrad / Wakil Kepala Staf",
            requiredRank = MilitaryRank.LETJEN,
            unitCategory = "Komando Cadangan Strategis",
            troopsUnderCommand = 65000,
            jurisdiction = "Seluruh Wilayah Nasional Siaga 1",
            operationalBudgetMultiplier = 25.0,
            influenceBonus = 70,
            tacticalBonus = 55,
            description = "Jenderal bintang tiga pengendali divisi infanteri tempur siap gerak dalam hitungan jam ke seluruh pelosok tanah air."
        ),
        // Jenderal
        MilitaryPost(
            id = "armed_forces_commander_in_chief",
            name = "Panglima Angkatan Bersenjata (Pangab)",
            requiredRank = MilitaryRank.JENDERAL,
            unitCategory = "Markas Besar Gabungan (Mabes)",
            troopsUnderCommand = 420000,
            jurisdiction = "Seluruh Ruang Darat, Laut, Udara & Siber",
            operationalBudgetMultiplier = 45.0,
            influenceBonus = 95,
            tacticalBonus = 75,
            description = "Panglima tertinggi militer. Penasihat pertahanan Presiden dan komandan operasi gabungan seluruh matra republik."
        ),
        // Jenderal Besar
        MilitaryPost(
            id = "grand_marshal_supreme_commander",
            name = "Panglima Tertinggi Pertahanan Total",
            requiredRank = MilitaryRank.JENDERAL_BESAR,
            unitCategory = "Dewan Pertahanan Nasional Tertinggi",
            troopsUnderCommand = 1200000,
            jurisdiction = "Pertahanan Total Kedaulatan Negara",
            operationalBudgetMultiplier = 100.0,
            influenceBonus = 100,
            tacticalBonus = 100,
            description = "Gelar bintang lima abadi. Mengarsiteki doktrin pertahanan total yang mematahkan invasi modern 2026."
        )
    )

    fun getPostForRank(rank: MilitaryRank): MilitaryPost {
        return ALL_POSTS.find { it.requiredRank == rank } ?: ALL_POSTS.first()
    }
}
