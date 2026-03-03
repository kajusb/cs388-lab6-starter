package com.codepath.lab6

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "ParksFragment"
private const val PARKS_URL = "https://developer.nps.gov/api/v1/parks"

class ParksFragment : Fragment() {

    private lateinit var parksRecyclerView: RecyclerView
    private lateinit var parksAdapter: ParksAdapter
    private val parks = mutableListOf<Park>()   // <-- Park should be your model type

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_parks, container, false)

        parksRecyclerView = view.findViewById(R.id.parks)
        parksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        parksRecyclerView.setHasFixedSize(true)

        parksAdapter = ParksAdapter(requireContext(), parks)
        parksRecyclerView.adapter = parksAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchParks()
    }

    private fun fetchParks() {
        val url = "$PARKS_URL?api_key=${BuildConfig.API_KEY}"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch parks: $statusCode\n$response", throwable)
            }

            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JSON
            ) {
                Log.i(TAG, "Successfully fetched parks")

                try {
                    val parsed = createJson().decodeFromString(
                        ParksResponse.serializer(),
                        json.jsonObject.toString()
                    )

                    val list = parsed.data ?: emptyList()
                    parks.clear()
                    parks.addAll(list)
                    parksAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Log.e(TAG, "JSON exception", e)
                } catch (e: Exception) {
                    Log.e(TAG, "Parsing exception", e)
                }
            }
        })
    }

    companion object {
        fun newInstance(): ParksFragment = ParksFragment()
    }
}