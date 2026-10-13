package com.example.model

data class EventOption(
    val title: String,
    val description: String,
    val leadershipDelta: Int = 0,
    val tacticalDelta: Int = 0,
    val disciplineDelta: Int = 0,
    val physicalDelta: Int = 0,
    val influenceDelta: Int = 0,
    val moraleDelta: Int = 0,
    val meritDelta: Int = 0,
    val budgetDelta: Long = 0,
    val resultNarrative: String
)

data class GeopoliticalEvent(
    val year: Int,
    val month: Int,
    val headline: String,
    val sender: String, // "Mabes TNI", "Kementerian Pertahanan", "Dewan Keamanan Nasional", "Badan Intelijen"
    val backgroundInfo: String,
    val options: List<EventOption>
)

object GeopoliticalTimelineRegistry {
    val EVENTS = listOf(
        GeopoliticalEvent(
            year = 2000,
            month = 7,
            headline = "Upacara Praspa 2000 & Sumpah Prajurit",
            sender = "Gubernur Akademi Militer",
            backgroundInfo = "Hari penentuan kelulusan tiba. Di hadapan panji kebesaran dan presiden, para kadet mengucapkan Sumpah Prajurit untuk setia kepada Pancasila dan kedaulatan rakyat. Anda resmi dilantik menjadi Letnan Dua!",
            options = listOf(
                EventOption(
                    title = "Pilih Penugasan Batalyon Perbatasan Terdepan",
                    description = "Meminta penempatan langsung di garis perbatasan rimba untuk membuktikan dedikasi tempur murni.",
                    leadershipDelta = +5,
                    tacticalDelta = +8,
                    disciplineDelta = +5,
                    influenceDelta = +2,
                    meritDelta = +25,
                    resultNarrative = "Anda ditempatkan sebagai Komandan Peleton Senapan di Batalyon Infanteri Perbatasan. Moral prajurit muda berkobar!"
                ),
                EventOption(
                    title = "Pilih Staf Intelijen & Analisis Strategis Mabes",
                    description = "Mengambil jalur staf perencana operasi untuk memperdalam pemahaman peta geopolitik nasional.",
                    leadershipDelta = +2,
                    tacticalDelta = +4,
                    disciplineDelta = +6,
                    influenceDelta = +10,
                    meritDelta = +20,
                    resultNarrative = "Anda ditugaskan di Biro Analisis Situasi Mabes, memperluas jejaring dan wawasan dengan para jenderal senior."
                )
            )
        ),
        GeopoliticalEvent(
            year = 2001,
            month = 4,
            headline = "Reformasi Doktrin Militer: Netralitas & Profesionalisme",
            sender = "Markas Besar Gabungan",
            backgroundInfo = "Era baru menuntut tentara yang murni profesional, menjunjung supremasi hukum, dan melepaskan seluruh fungsi politik praktis.",
            options = listOf(
                EventOption(
                    title = "Tegakkan Disiplin Keras Tanpa Kompromi",
                    description = "Menolak segala intervensi luar dan melatih anak buah fokus pada taktik tempur dan hukum perang.",
                    disciplineDelta = +12,
                    tacticalDelta = +6,
                    leadershipDelta = +4,
                    meritDelta = +30,
                    resultNarrative = "Satuan Anda diakui Mabes sebagai teladan integritas perwira generasi baru reformasi."
                ),
                EventOption(
                    title = "Bangun Komunikasi Humanis dengan Warga Sipil",
                    description = "Mengedepankan kemanunggalan TNI dengan rakyat di desa-desa terpencil.",
                    leadershipDelta = +10,
                    moraleDelta = +8,
                    influenceDelta = +6,
                    meritDelta = +25,
                    resultNarrative = "Penduduk setempat menaruh rasa hormat mendalam pada satuan Anda, pasokan informasi intelijen mengalir lancar."
                )
            )
        ),
        GeopoliticalEvent(
            year = 2004,
            month = 12,
            headline = "Tragedi Megathrust: Operasi Militer Selain Perang (OMSP)",
            sender = "Pusat Komando Tanggap Darurat Nasional",
            backgroundInfo = "Gelombang tsunami raksasa menghancurkan pesisir. Ribuan warga membutuhkan evakuasi darurat, air bersih, dan jembatan zeni segera.",
            options = listOf(
                EventOption(
                    title = "Pimpin Pendaratan Bantuan di Wilayah Terisolir",
                    description = "Kerahkan seluruh peleton menembus puing dan lumpur untuk mendirikan posko medis garis depan.",
                    leadershipDelta = +15,
                    moraleDelta = +12,
                    physicalDelta = -5,
                    meritDelta = +50,
                    resultNarrative = "Aksi heroik Anda menyelamatkan ratusan nyawa keluarga. Mabes menganugerahkan Tanda Jasa Operasi Kemanusiaan."
                ),
                EventOption(
                    title = "Amankan Jalur Logistik Distribusi Bantuan Internasional",
                    description = "Pastikan bantuan obat-obatan dan tenda terdistribusi merata tanpa penjarahan liar.",
                    leadershipDelta = +8,
                    disciplineDelta = +10,
                    tacticalDelta = +8,
                    meritDelta = +40,
                    resultNarrative = "Kelancaran logistik diapresiasi tim bantuan PBB dan delegasi pertahanan sahabat."
                )
            )
        ),
        GeopoliticalEvent(
            year = 2008,
            month = 6,
            headline = "Ketegangan Blok Karang Perbatasan Maritim",
            sender = "Badan Intelijen Strategis",
            backgroundInfo = "Kapal perang patroli negara tetangga berulang kali bermanuver provokatif melintasi batas ZEE di perairan mercusuar terluar.",
            options = listOf(
                EventOption(
                    title = "Gelar Operasi Siaga Bayang-Bayang (Shadowing)",
                    description = "Bermanuver tegas menghadang laju kapal asing dengan prosedur pengusiran damai namun tanpa mundur sejengkal pun.",
                    tacticalDelta = +12,
                    leadershipDelta = +10,
                    disciplineDelta = +8,
                    meritDelta = +60,
                    resultNarrative = "Manuver presisi Anda memaksa armada asing memutar haluan tanpa ada letusan peluru yang membakar perang."
                ),
                EventOption(
                    title = "Perkuat Pos Pengamatan Radar & Rudal Pesisir",
                    description = "Menyiagakan baterai artileri pertahanan pantai sebagai sinyal kesiapan tempur mutlak.",
                    tacticalDelta = +10,
                    influenceDelta = +8,
                    meritDelta = +45,
                    resultNarrative = "Kesiapsiagaan alutsista Anda membuat Mabes yakin akan ketangguhan doktrin pertahanan berlapis."
                )
            )
        ),
        GeopoliticalEvent(
            year = 2012,
            month = 8,
            headline = "Cetak Biru Minimum Essential Force (MEF)",
            sender = "Kementerian Pertahanan Republik",
            backgroundInfo = "Pemerintah mengalokasikan anggaran modernisasi: pemilihan tank tempur utama, jet tempur multiperan, dan senapan serbu generasi baru.",
            options = listOf(
                EventOption(
                    title = "Dorong Alutsista Buatan Industri Pertahanan Dalam Negeri",
                    description = "Mengutamakan tank dan panser buatan anak bangsa serta senapan serbu lokal.",
                    influenceDelta = +15,
                    tacticalDelta = +8,
                    budgetDelta = +50_000_000,
                    meritDelta = +70,
                    resultNarrative = "Kemandirian industri pertahanan melonjak! Anda dipuji sebagai perwira visioner berjiwa nasionalis kuat."
                ),
                EventOption(
                    title = "Beli Alutsista Canggih Berstandar NATO/Global",
                    description = "Memilih teknologi mutakhir tercanggih untuk mengejar ketertinggalan teknologi militer.",
                    tacticalDelta = +16,
                    leadershipDelta = +8,
                    meritDelta = +60,
                    resultNarrative = "Prajurit Anda mendapatkan pelatihan sistem persenjataan berteknologi tinggi generasi baru."
                )
            )
        ),
        GeopoliticalEvent(
            year = 2018,
            month = 10,
            headline = "Pembentukan Satuan Khusus Tiga Matra (Koopsus)",
            sender = "Panglima Angkatan Bersenjata",
            backgroundInfo = "Ancaman terorisme global dan pembajakan membutuhkan unit pemukul elit gabungan dari unsur darat, laut, dan udara.",
            options = listOf(
                EventOption(
                    title = "Kirim Kontingen Pasukan Raider Terbaik Anda",
                    description = "Menyeleksi prajurit berkualifikasi komando tertinggi untuk menjadi tulang punggung Koopsus.",
                    leadershipDelta = +12,
                    tacticalDelta = +14,
                    moraleDelta = +10,
                    meritDelta = +90,
                    resultNarrative = "Pasukan binaan Anda menjadi inti kekuatan pemukul rahasia negara, nama Anda harum di dewan Wanjakti."
                ),
                EventOption(
                    title = "Rancang Doktrin Integrasi Intelijen Satelit & Siber",
                    description = "Membangun sistem komando C4ISR digital untuk mendukung operasi khusus tanpa jeda detik.",
                    tacticalDelta = +18,
                    influenceDelta = +15,
                    meritDelta = +85,
                    resultNarrative = "Sistem C4ISR yang Anda bangun menjadi standar operasional seluruh matra militer republik."
                )
            )
        ),
        GeopoliticalEvent(
            year = 2023,
            month = 3,
            headline = "Evolusi Peperangan Drone Kamikaze & Pertahanan Udara",
            sender = "Pusat Pengkajian Strategis Militer",
            backgroundInfo = "Konflik dunia membuktikan peran drone murah namun mematikan. Pangkalan militer harus segera dilengkapi sistem anti-drone frekuensi tinggi.",
            options = listOf(
                EventOption(
                    title = "Penggelaran Perangkat Jammer & Kubah Anti-Drone",
                    description = "Melindungi seluruh pos komando dan baterai artileri dengan perisai frekuensi mikro.",
                    tacticalDelta = +20,
                    disciplineDelta = +10,
                    budgetDelta = -100_000_000,
                    meritDelta = +120,
                    resultNarrative = "Satuan Anda kebal terhadap pengintaian udara dan serangan drone liar musuh."
                ),
                EventOption(
                    title = "Bentuk Batalyon Drone Serbu FPV Organik",
                    description = "Melatih prajurit infanteri menjadi operator drone kamikaze yang mematikan dan mobile.",
                    tacticalDelta = +24,
                    leadershipDelta = +14,
                    meritDelta = +130,
                    resultNarrative = "Daya hancur satuan Anda meningkat pesat, mengubah taktik medan perang secara radikal!"
                )
            )
        ),
        GeopoliticalEvent(
            year = 2026,
            month = 1,
            headline = "Krisis Eksistensial 2026: Armada Asing di Ambang Pintu",
            sender = "Dewan Ketahanan Nasional Tertinggi",
            backgroundInfo = "Eskalasi global memuncak. Armada koalisi adidaya mendekati laut kedaulatan dengan ultimatum intimidasi. Presiden memanggil Anda ke ruang komando bawah tanah!",
            options = listOf(
                EventOption(
                    title = "Umumkan Doktrin Pertahanan Rakyat Total (Hanneg)",
                    description = "Mobilisasi cadangan nasional, sebar baterai rudal bergerak di seribu pulau, dan bersiap perang atrisi.",
                    leadershipDelta = +30,
                    tacticalDelta = +25,
                    moraleDelta = +25,
                    influenceDelta = +30,
                    meritDelta = +250,
                    resultNarrative = "Tekad baja Anda membakar semangat juang seluruh bangsa. Armada musuh tertegun menghadapi benteng kepulauan tak tertembus!"
                ),
                EventOption(
                    title = "Lancarkan Serangan Tangkal Presisi Elektronik Jarak Jauh",
                    description = "Lumpuhkan sistem pemandu satelit armada musuh lewat serangan siber terkoordinasi dan rudal pesisir.",
                    tacticalDelta = +35,
                    leadershipDelta = +20,
                    influenceDelta = +25,
                    meritDelta = +260,
                    resultNarrative = "Kemenangan spektakuler! Sistem navigasi armada musuh lumpuh seketika, memaksa mereka menandatangani perjanjian damai!"
                )
            )
        )
    )

    fun getEventForYearAndMonth(year: Int, month: Int): GeopoliticalEvent? {
        return EVENTS.find { it.year == year && it.month == month }
    }
}
