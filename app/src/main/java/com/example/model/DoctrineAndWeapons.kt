package com.example.model

enum class EquipmentCategory(val title: String) {
    SENJATA_PERORANGAN("Senjata Perorangan"),
    RANPUR_ARMOR("Kendaraan Tempur Lapis Baja"),
    ARTILERI_MEDAN("Bantuan Artileri & Roket"),
    DRONE_DAN_SIBER("Drone & Perang Elektronika")
}

data class MilitaryArsenalItem(
    val id: String,
    val name: String,
    val category: EquipmentCategory,
    val yearIntroduced: Int,
    val cost: Long,
    val tacticalBonus: Int,
    val isAcquired: Boolean = false,
    val description: String
)

data class MilitaryDoctrine(
    val id: String,
    val name: String,
    val era: String,
    val meritRequired: Int,
    val leadershipBonus: Int,
    val tacticalBonus: Int,
    val isAdopted: Boolean = false,
    val description: String
)

object ArsenalAndDoctrineRegistry {
    val INITIAL_ARSENAL = listOf(
        MilitaryArsenalItem(
            id = "rifle_ss1",
            name = "Senapan Serbu SS-1 V1 (5.56mm)",
            category = EquipmentCategory.SENJATA_PERORANGAN,
            yearIntroduced = 2000,
            cost = 25_000_000,
            tacticalBonus = 5,
            isAcquired = true,
            description = "Senapan andalan prajurit republik di awal milenium dengan laras tahan cuaca tropis."
        ),
        MilitaryArsenalItem(
            id = "rifle_ss2",
            name = "Senapan Serbu SS-2 V4 Picatinny Rail",
            category = EquipmentCategory.SENJATA_PERORANGAN,
            yearIntroduced = 2008,
            cost = 65_000_000,
            tacticalBonus = 12,
            isAcquired = false,
            description = "Akurasi tinggi pemenang lomba tembak AASAM internasional, dilengkapi optik bidik cepat."
        ),
        MilitaryArsenalItem(
            id = "armor_anoa",
            name = "Panser Pengangkut Personel Anoa 6x6",
            category = EquipmentCategory.RANPUR_ARMOR,
            yearIntroduced = 2009,
            cost = 350_000_000,
            tacticalBonus = 22,
            isAcquired = false,
            description = "Panser monocoque berlapis baja tahan munisi kaliber 7.62mm dan ranjau darat."
        ),
        MilitaryArsenalItem(
            id = "armor_harimau",
            name = "Tank Tempur Medium Harimau MMWT",
            category = EquipmentCategory.RANPUR_ARMOR,
            yearIntroduced = 2018,
            cost = 950_000_000,
            tacticalBonus = 35,
            isAcquired = false,
            description = "Meriam Cockerill 105mm dengan sistem perlindungan aktif Pulsa Laser dan mobilitas pulau tinggi."
        ),
        MilitaryArsenalItem(
            id = "artillery_astros",
            name = "Baterai Peluncur Roket MLRS Astros II",
            category = EquipmentCategory.ARTILERI_MEDAN,
            yearIntroduced = 2014,
            cost = 1_200_000_000,
            tacticalBonus = 45,
            isAcquired = false,
            description = "Daya hancur area saturasi hingga radius 80km untuk mematahkan gelombang formasi lawan."
        ),
        MilitaryArsenalItem(
            id = "drone_kamikaze_rajawali",
            name = "Kawanan Drone Kamikaze FPV Rajawali",
            category = EquipmentCategory.DRONE_DAN_SIBER,
            yearIntroduced = 2023,
            cost = 750_000_000,
            tacticalBonus = 50,
            isAcquired = false,
            description = "Sistem serbu drone berpemandu kecerdasan buatan optik anti-jamming frekuensi."
        ),
        MilitaryArsenalItem(
            id = "c4isr_cyber_shield",
            name = "Perisai Perang Elektronika C4ISR 2026",
            category = EquipmentCategory.DRONE_DAN_SIBER,
            yearIntroduced = 2026,
            cost = 2_000_000_000,
            tacticalBonus = 70,
            isAcquired = false,
            description = "Kubah pertahanan siber taktis pemutus rantai komando satelit dan pemandu rudal musuh."
        )
    )

    val DOCTRINES = listOf(
        MilitaryDoctrine(
            id = "doc_sishankamrata",
            name = "Doktrin Sishankamrata & Perang Rimba",
            era = "Era 2000-an",
            meritRequired = 100,
            leadershipBonus = 10,
            tacticalBonus = 10,
            isAdopted = true,
            description = "Memadukan ketangguhan survival prajurit dengan dukungan total rakyat di kawasan terpencil."
        ),
        MilitaryDoctrine(
            id = "doc_raider_assault",
            name = "Doktrin Serbuan Cepat Raider Tiga Matra",
            era = "Era 2010-an",
            meritRequired = 500,
            leadershipBonus = 18,
            tacticalBonus = 22,
            isAdopted = false,
            description = "Taktik infiltrasi kilat rawa laut, mobilitas udara heliborne, dan penyerbuan malam serempak."
        ),
        MilitaryDoctrine(
            id = "doc_island_defense",
            name = "Doktrin Pertahanan Kepulauan Berlapis",
            era = "Era 2020-an",
            meritRequired = 1800,
            leadershipBonus = 30,
            tacticalBonus = 35,
            isAdopted = false,
            description = "Penyebaran baterai rudal pesisir bergerak di selat sempit untuk mematikan armada invasi."
        ),
        MilitaryDoctrine(
            id = "doc_multi_domain_2026",
            name = "Doktrin Perang Multi-Domain Holistik 2026",
            era = "Era 2026",
            meritRequired = 4500,
            leadershipBonus = 50,
            tacticalBonus = 60,
            isAdopted = false,
            description = "Integrasi total ruang darat, laut, udara, antariksa, siber, dan medan elektromagnetik."
        )
    )
}
