package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*
import org.json.JSONArray
import org.json.JSONObject

class GameRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("milsim_officer_save_data", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PROFILE_DATA = "key_profile_data"
        private const val KEY_ACTIVE_MISSION_ID = "key_active_mission_id"
        private const val KEY_MISSION_VICTORIES = "key_mission_victories"
    }

    fun saveProfile(profile: PlayerProfile) {
        val json = JSONObject().apply {
            put("name", profile.name)
            put("nrp", profile.nrp)
            put("birthYear", profile.birthYear)
            put("currentYear", profile.currentYear)
            put("currentMonth", profile.currentMonth)
            put("rankName", profile.rank.name)
            put("postId", profile.post.id)
            put("branchName", profile.branch.name)
            put("monthsInCurrentRank", profile.monthsInCurrentRank)
            put("leadership", profile.leadership)
            put("tacticalAcumen", profile.tacticalAcumen)
            put("discipline", profile.discipline)
            put("physicalCondition", profile.physicalCondition)
            put("politicalInfluence", profile.politicalInfluence)
            put("troopsMorale", profile.troopsMorale)
            put("meritPoints", profile.meritPoints)
            put("operationalFunds", profile.operationalFunds)
            put("missionsCompleted", profile.missionsCompleted)
            put("tacticalVictories", profile.tacticalVictories)
            put("seskoadPassed", profile.seskoadPassed)
            put("lemhannasPassed", profile.lemhannasPassed)

            // Save Brevets
            val brevetsArray = JSONArray()
            profile.brevets.forEach { brevetsArray.put(it) }
            put("brevets", brevetsArray)

            // Save Logs (latest 30)
            val logsArray = JSONArray()
            profile.careerLogs.takeLast(30).forEach { log ->
                val logObj = JSONObject().apply {
                    put("year", log.year)
                    put("month", log.month)
                    put("title", log.title)
                    put("details", log.details)
                    put("isMilestone", log.isMilestone)
                }
                logsArray.put(logObj)
            }
            put("careerLogs", logsArray)

            // Save Medals
            val medalsArray = JSONArray()
            profile.medals.forEach { medal ->
                val medalObj = JSONObject().apply {
                    put("id", medal.id)
                    put("name", medal.name)
                    put("ribbonColorHex", medal.ribbonColorHex)
                    put("description", medal.description)
                    put("prestigeBonus", medal.prestigeBonus)
                }
                medalsArray.put(medalObj)
            }
            put("medals", medalsArray)
        }

        prefs.edit().putString(KEY_PROFILE_DATA, json.toString()).apply()
    }

    fun loadProfile(): PlayerProfile {
        val raw = prefs.getString(KEY_PROFILE_DATA, null) ?: return defaultProfile()
        return try {
            val json = JSONObject(raw)
            val rankName = json.optString("rankName", MilitaryRank.KADET.name)
            val rank = runCatching { MilitaryRank.valueOf(rankName) }.getOrDefault(MilitaryRank.KADET)
            val postId = json.optString("postId", "")
            val post = MilitaryPostsRegistry.ALL_POSTS.find { it.id == postId }
                ?: MilitaryPostsRegistry.getPostForRank(rank)
            val branchName = json.optString("branchName", MilitaryBranch.INFANTERI.name)
            val branch = runCatching { MilitaryBranch.valueOf(branchName) }.getOrDefault(MilitaryBranch.INFANTERI)

            val brevetsList = mutableListOf<String>()
            val brevetsArray = json.optJSONArray("brevets")
            if (brevetsArray != null) {
                for (i in 0 until brevetsArray.length()) {
                    brevetsList.add(brevetsArray.getString(i))
                }
            } else {
                brevetsList.add("Yudha Wastu Pramuka (Infanteri)")
            }

            val logsList = mutableListOf<CareerEventLog>()
            val logsArray = json.optJSONArray("careerLogs")
            if (logsArray != null) {
                for (i in 0 until logsArray.length()) {
                    val item = logsArray.getJSONObject(i)
                    logsList.add(
                        CareerEventLog(
                            year = item.optInt("year", 2000),
                            month = item.optInt("month", 1),
                            title = item.optString("title", "Catatan Dinas"),
                            details = item.optString("details", ""),
                            isMilestone = item.optBoolean("isMilestone", false)
                        )
                    )
                }
            }

            val medalsList = mutableListOf<MilitaryMedal>()
            val medalsArray = json.optJSONArray("medals")
            if (medalsArray != null) {
                for (i in 0 until medalsArray.length()) {
                    val item = medalsArray.getJSONObject(i)
                    medalsList.add(
                        MilitaryMedal(
                            id = item.optString("id", "medal"),
                            name = item.optString("name", "Tanda Jasa"),
                            ribbonColorHex = item.optString("ribbonColorHex", "#D4AF37"),
                            description = item.optString("description", ""),
                            prestigeBonus = item.optInt("prestigeBonus", 5)
                        )
                    )
                }
            } else {
                medalsList.addAll(defaultProfile().medals)
            }

            PlayerProfile(
                name = json.optString("name", "Aria Sena"),
                nrp = json.optString("nrp", "112000-089"),
                birthYear = json.optInt("birthYear", 1978),
                currentYear = json.optInt("currentYear", 2000),
                currentMonth = json.optInt("currentMonth", 1),
                rank = rank,
                post = post,
                branch = branch,
                monthsInCurrentRank = json.optInt("monthsInCurrentRank", 0),
                leadership = json.optInt("leadership", 65),
                tacticalAcumen = json.optInt("tacticalAcumen", 60),
                discipline = json.optInt("discipline", 78),
                physicalCondition = json.optInt("physicalCondition", 85),
                politicalInfluence = json.optInt("politicalInfluence", 30),
                troopsMorale = json.optInt("troopsMorale", 75),
                meritPoints = json.optInt("meritPoints", 25),
                operationalFunds = json.optLong("operationalFunds", 150_000_000L),
                missionsCompleted = json.optInt("missionsCompleted", 0),
                tacticalVictories = json.optInt("tacticalVictories", 0),
                seskoadPassed = json.optBoolean("seskoadPassed", false),
                lemhannasPassed = json.optBoolean("lemhannasPassed", false),
                medals = medalsList,
                brevets = brevetsList,
                careerLogs = if (logsList.isEmpty()) defaultProfile().careerLogs else logsList
            )
        } catch (e: Exception) {
            defaultProfile()
        }
    }

    fun resetGame(): PlayerProfile {
        prefs.edit().clear().apply()
        return defaultProfile()
    }

    private fun defaultProfile(): PlayerProfile {
        return PlayerProfile()
    }
}
