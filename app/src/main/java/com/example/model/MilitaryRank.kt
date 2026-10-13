package com.example.model

/**
 * Sistem Kepangkatan Militer Fiksi Realistis (Tentara Nasional Republik / TNR)
 * Lengkap dari Kadet Akademi hingga Jenderal Besar Bintang 5.
 */
enum class RankTier(val label: String) {
    AKADEMI("Pendidikan Perwira"),
    PAMA("Perwira Pertama"),
    PAMEN("Perwira Menengah"),
    PATI("Perwira Tinggi")
}

enum class MilitaryRank(
    val title: String,
    val abbreviation: String,
    val tier: RankTier,
    val minMeritPoints: Int,
    val minYearsOfService: Int,
    val baseMonthlySalary: Long, // Dalam Rupiah (skala tahun berjalan)
    val starsOrEmblemCount: Int, // 1-5 bintang / melati / balok
    val description: String
) {
    KADET(
        title = "Sersan Mayor Taruna (Kadet)",
        abbreviation = "Sermatar",
        tier = RankTier.AKADEMI,
        minMeritPoints = 0,
        minYearsOfService = 0,
        baseMonthlySalary = 850_000,
        starsOrEmblemCount = 1,
        description = "Tingkat akhir Akademi Militer Lembah Tidar tahun 2000. Memimpin batalyon korps kadet menjelang Praspa."
    ),
    LETDA(
        title = "Letnan Dua",
        abbreviation = "Letda",
        tier = RankTier.PAMA,
        minMeritPoints = 120,
        minYearsOfService = 0,
        baseMonthlySalary = 2_800_000,
        starsOrEmblemCount = 1,
        description = "Perwira Pertama termuda. Memimpin 30-40 prajurit dalam Peleton Senapan di garis depan perbatasan."
    ),
    LETTU(
        title = "Letnan Satu",
        abbreviation = "Lettu",
        tier = RankTier.PAMA,
        minMeritPoints = 320,
        minYearsOfService = 4,
        baseMonthlySalary = 3_400_000,
        starsOrEmblemCount = 2,
        description = "Perwira Peleton Senior dan Wakil Komandan Kompi. Bertanggung jawab atas taktik dan kesiapan tempur regu."
    ),
    KAPTEN(
        title = "Kapten",
        abbreviation = "Kapt",
        tier = RankTier.PAMA,
        minMeritPoints = 650,
        minYearsOfService = 8,
        baseMonthlySalary = 4_500_000,
        starsOrEmblemCount = 3,
        description = "Komandan Kompi (Danki). Memimpin 120-150 prajurit dalam operasi taktis ofensif dan defensif."
    ),
    MAYOR(
        title = "Mayor",
        abbreviation = "May",
        tier = RankTier.PAMEN,
        minMeritPoints = 1100,
        minYearsOfService = 12,
        baseMonthlySalary = 6_200_000,
        starsOrEmblemCount = 1,
        description = "Perwira Menengah pertama (1 Melati Emas). Wakil Danyon atau Kepala Seksi Operasi Staf Brigade."
    ),
    LETKOL(
        title = "Letnan Kolonel",
        abbreviation = "Letkol",
        tier = RankTier.PAMEN,
        minMeritPoints = 1750,
        minYearsOfService = 16,
        baseMonthlySalary = 8_500_000,
        starsOrEmblemCount = 2,
        description = "Komandan Batalyon (Danyon) atau Komandan Distrik Militer (Dandim). Memimpin 700-900 prajurit tempur teritorial."
    ),
    KOLONEL(
        title = "Kolonel",
        abbreviation = "Kol",
        tier = RankTier.PAMEN,
        minMeritPoints = 2600,
        minYearsOfService = 20,
        baseMonthlySalary = 12_000_000,
        starsOrEmblemCount = 3,
        description = "Komandan Brigade Infanteri (Danbrigif) atau Komandan Korem (Danrem). Membawahi multi-batalyon dan wilayah provinsi."
    ),
    BRIGJEN(
        title = "Brigadir Jenderal",
        abbreviation = "Brigjen",
        tier = RankTier.PATI,
        minMeritPoints = 3700,
        minYearsOfService = 23,
        baseMonthlySalary = 18_000_000,
        starsOrEmblemCount = 1,
        description = "Jenderal Bintang Satu. Kepala Staf Kodam (Kasdam), Komandan Pasukan Khusus Reaksi Cepat, atau Direktur Strategis."
    ),
    MAYJEN(
        title = "Mayor Jenderal",
        abbreviation = "Mayjen",
        tier = RankTier.PATI,
        minMeritPoints = 5000,
        minYearsOfService = 25,
        baseMonthlySalary = 25_000_000,
        starsOrEmblemCount = 2,
        description = "Jenderal Bintang Dua. Panglima Komando Daerah Militer (Pangdam) memimpin puluhan ribu prajurit dan pertahanan pulau."
    ),
    LETJEN(
        title = "Letnan Jenderal",
        abbreviation = "Letjen",
        tier = RankTier.PATI,
        minMeritPoints = 6600,
        minYearsOfService = 26,
        baseMonthlySalary = 34_000_000,
        starsOrEmblemCount = 3,
        description = "Jenderal Bintang Tiga. Panglima Kostrad atau Kepala Staf Angkatan Darat (KSAD), pengendali seluruh doktrin dan kesiapan angkatan."
    ),
    JENDERAL(
        title = "Jenderal",
        abbreviation = "Jend",
        tier = RankTier.PATI,
        minMeritPoints = 8500,
        minYearsOfService = 26,
        baseMonthlySalary = 48_000_000,
        starsOrEmblemCount = 4,
        description = "Jenderal Bintang Empat. Panglima Angkatan Bersenjata Republik. Komando tertinggi operasional seluruh matra militer bangsa."
    ),
    JENDERAL_BESAR(
        title = "Jenderal Besar",
        abbreviation = "Jend.Besar",
        tier = RankTier.PATI,
        minMeritPoints = 11000,
        minYearsOfService = 26,
        baseMonthlySalary = 75_000_000,
        starsOrEmblemCount = 5,
        description = "Jenderal Bintang Lima Kehormatan. Pahlawan Tertinggi Pertahanan Total. Pemimpin kemenangan krisis geopolitik eksistensial 2026."
    );

    fun nextRank(): MilitaryRank? {
        val nextOrdinal = ordinal + 1
        return if (nextOrdinal < values().size) values()[nextOrdinal] else null
    }

    fun previousRank(): MilitaryRank? {
        val prevOrdinal = ordinal - 1
        return if (prevOrdinal >= 0) values()[prevOrdinal] else null
    }
}
