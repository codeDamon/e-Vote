package com.example.e_votemvvm.Repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.e_votemvvm.Databases.PostDatabaseDao
import com.example.e_votemvvm.Models.Party
import com.example.e_votemvvm.Models.Post
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class PostRepository(private val postDatabaseDao: PostDatabaseDao) {

    val getAllPosts: LiveData<List<Post>> = postDatabaseDao.getAllPosts()


    suspend fun insert(post: Post){
        postDatabaseDao.insertPost(post)
    }

    suspend fun insertPosts(list: List<Post>){
            //postDatabaseDao.insertAllPost(fillList())
            //fillListUsingFirebase()
            postDatabaseDao.insertAllPost(list)
    }

    suspend fun delete(post: Post){
        postDatabaseDao.deletePost(post)
    }

    suspend fun deleteAll(){
        postDatabaseDao.deleteAllPosts()
    }

    suspend fun getPost(id: Long):Post?{
        return postDatabaseDao.getPost(id)
    }

    private fun fillList() : ArrayList<Post>{

        val list : ArrayList<Post> = ArrayList()

        val parties: ArrayList<Party> = ArrayList()

        var url = "https://4.bp.blogspot.com/-maWjx4TelnU/TjKM45tgl5I/AAAAAAAAAKE/TE0yMkDU0GM/s1600/BJP_logo.jpg"
        var partyTemp : Party = Party("Bharatiya Janata Party", url, "Narendra Modi", "BJP", false)
        parties.add(partyTemp)

        url="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Falochonaa.files.wordpress.com%2F2014%2F02%2Fcongress-logo.jpg&f=1&nofb=1"
        partyTemp = Party("Indian National Congress", url, "Sonia Gandhi", "INC", false)
        parties.add(partyTemp)

        url="https://images.news18.com/ibnlive/uploads/2016/10/aap.jpg?impolicy=website&width=536&height=356"
        partyTemp = Party("Aam Aadami Party", url, "Arvind Kejriwal", "AAP", false)
        parties.add(partyTemp)

        url="https://pbs.twimg.com/profile_images/497400199333965824/2FwsZJrK.jpeg"
        partyTemp = Party("Shiv Sena", url, "Uddav Thackeray", "SS", false)
        parties.add(partyTemp)
        parties.add(partyTemp)
        parties.add(partyTemp)

        val gson = Gson();
        val pp: String = gson.toJson(parties)



        var postObj: Post = Post(
            "Lok Sabha Election", "23 Jan 2021 11:00",
            "24 Jan 2021 18:00", pp, "details_here"
        )
        list.add(postObj)



        postObj = Post(
            "State Assembly Election", "30 Jan 2021 06:00",
            "31 Jan 2021 06:00", pp, "details_here"
        )
        list.add(postObj)


        postObj = Post(
            "Panchayat Election", "23 Feb 2021 08:00",
            "24 Feb 2021 08:00", pp, "details_here"
        )
        list.add(postObj)


        postObj = Post(
            "Municipal Corporation Election", "23 Jan 2021 00:00",
            "24 Jan 2021 07:59", pp, "details_here"
        )
        list.add(postObj)

        return list

    }

    private fun fillListUsingFirebase():ArrayList<Post>{

        val list: ArrayList<Post> = ArrayList()


            val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("posts")
            val check: Query = ref.orderByKey().limitToFirst(1)

            check.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    //Log.d("POST", snapshot.value.toString())
                    for (child in snapshot.children) {

                        //Log.d("POST", child.value.toString())


                        Log.d("POST", child.child("post_name").value.toString())
                        Log.d("POST", child.child("post_start").value.toString())
                        Log.d("POST", child.child("post_end").value.toString())
                        Log.d("POST", child.child("post_details").value.toString())
                        Log.d("POST", child.child("parties").child("AAP").value.toString())

                        val p_name = child.child("post_name").value.toString()
                        val p_start = child.child("post_start").value.toString()
                        val p_end = child.child("post_end").value.toString()
                        val p_details = child.child("post_details").value.toString()


                        val parties: ArrayList<Party> = ArrayList()

                        for (party in child.child("parties").children) {
                            Log.d("POST", party.child("logo").value.toString())
                            parties.add(
                                Party(
                                    party.child("name").value.toString(),
                                    party.child("logo").value.toString(),
                                    party.child("leader").value.toString(),
                                    party.child("short_name").value.toString(),
                                    false
                                )
                            )
                        }

                        val gson = Gson();
                        val parties_str: String = gson.toJson(parties)

                        list.add(Post(p_name, p_start, p_end, parties_str, p_details))


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("POST", "No Data")
                }

            })
        return list
    }

}