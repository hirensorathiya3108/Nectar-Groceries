package com.nectar.groceries.nectargroceries.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.model.products.ProductOffer
import com.nectar.groceries.nectargroceries.data.model.products.Products
import com.nectar.groceries.nectargroceries.database.FirebaseDB
import com.nectar.groceries.nectargroceries.databinding.FragmentShopBinding
import com.nectar.groceries.nectargroceries.extensions.beGone
import com.nectar.groceries.nectargroceries.extensions.beVisible
import com.nectar.groceries.nectargroceries.ui.adapter.BestSellingProductAdapter
import com.nectar.groceries.nectargroceries.ui.adapter.ExclusiveProductAdapter
import com.nectar.groceries.nectargroceries.ui.adapter.ImageAdapter
import com.nectar.groceries.nectargroceries.view.AdaptiveSpacingItemDecoration
import needle.Needle
import needle.UiRelatedTask
import android.provider.Settings
import com.nectar.groceries.nectargroceries.data.model.products.CartProductsData
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.utils.Utils


class ShopFragment : ParentFragment() {
    private lateinit var binding: FragmentShopBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference
    private lateinit var exclusiveProductList: ArrayList<Products>
    private lateinit var bestSellingProductList: ArrayList<Products>
    private lateinit var offerImageList: ArrayList<ProductOffer>

    companion object {
        fun newInstance(title: String): ShopFragment {
            val fragment = ShopFragment()
            val args = Bundle()
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity = requireActivity()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        exclusiveProductList = ArrayList()
        bestSellingProductList = ArrayList()
        offerImageList = ArrayList()
//        sendProductInFirebaseDataBase()
        getOfferDataList()
        getExclusiveOffer()
        getBestSelling()

    }

    private fun sendProductInFirebaseDataBase() {
        val documentReference = FirebaseDB().getCollectionReferenceForBestSelling()

        /*val itemList = arrayListOf(
            Pair(
                "product_1", Products(
                    "https://firebasestorage.googleapis.com/v0/b/nectargroceries-bab35.appspot.com/o/product_image%2Fexclusive_offer%2Fic_apple.png?alt=media&token=0d56c645-3129-4038-b1e7-6d31346f69fc",
                    "Apple - Shimla",
                    "Product image shown is for representation purpose only, the actually product may vary based on season, produce & availability.",
                    "Apples are one of the healthiest fruits. Gala apples are a significant source of these damage-fighting chemicals due to the presence of antioxidants.",
                    4.5,
                    "5,pcs",
                    "1kg",
                    "$4.99"
                )
            ),
            Pair(
                "product_2", Products(
                    "https://firebasestorage.googleapis.com/v0/b/nectargroceries-bab35.appspot.com/o/product_image%2Fexclusive_offer%2Fstrawberry.jpg?alt=media&token=893e7d43-99e3-4a88-a537-168f4b2f3d66",
                    "Strawberry",
                    "Firm and fibrous ginger roots are stretched with multiple fingers that have light to dark tan skin and rings on it and is aromatic, spicy and pungent.",
                    "Strawberries improve heart function.They are rich in antioxidants and detoxifiers, which reduce arthritis and gout pain.",
                    4.5,
                    "",
                    "200g",
                    "$1.5"
                )
            ),
            Pair(
                "product_3", Products(
                    "https://firebasestorage.googleapis.com/v0/b/nectargroceries-bab35.appspot.com/o/product_image%2Fexclusive_offer%2Fginger.png?alt=media&token=8b235b86-5c35-42fc-a433-d620fd9a1ac2",
                    "Ginger(Loose)",
                    "Firm and fibrous ginger roots are stretched with multiple fingers that have light to dark tan skin and rings on it and is aromatic, spicy and pungent.",
                    "Drinking ginger juice is a great medicine to relieve sore throats.",
                    4.5,
                    "",
                    "100g",
                    "$1"
                )
            ),
            Pair(
                "product_4", Products(
                    "https://firebasestorage.googleapis.com/v0/b/nectargroceries-bab35.appspot.com/o/product_image%2Fexclusive_offer%2FOrganicBananas.png?alt=media&token=3134c7db-cc2e-412b-ab2b-758bb4dd7d0e",
                    "Baby Banana",
                    "Robusta bananas have a very dense creamy texture and once ripe their flavour is rich and sweet.",
                    "One banana supplies 30 percent of the daily vitamin B6 requirement and is rich in vitamin C, potassium and fiber.",
                    4.5,
                    "(9-12pcs)",
                    "1kg",
                    "$3.99"
                )
            ),
        )

        for ((customId, item) in itemList) {
            documentReference.document(customId)
                .set(item)
                .addOnSuccessListener {
                    // Data uploaded successfully
                    // You can add any success handling logic here
                }
                .addOnFailureListener { e ->
                    // Handle the failure
                    Log.e("Firestore", "Error uploading item with custom ID $customId", e)
                }
        }*/
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getOfferDataList() {
        val documentReference = FirebaseDB().getCollectionReferenceForOffer()
        documentReference.addSnapshotListener { value, error ->
            val data = value?.toObjects(ProductOffer::class.java)
            Needle.onBackgroundThread().execute(object : UiRelatedTask<Void?>() {
                override fun doWork(): Void? {
                    offerImageList.addAll(data!!)
                    return null
                }

                override fun thenDoUiRelatedWork(result: Void?) {
                    if (offerImageList.size != 0) {
                        setSliderView()
                    }
                }
            })
        }
    }

    private fun setSliderView() {
        Log.e("thenDoUiRelatedWork: ", "runSetSlider => Enter")
        val imageAdapter = ImageAdapter()
        imageAdapter.submitList(offerImageList)
        binding.vpSlider.adapter = imageAdapter
        binding.vpSlider.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

        val dotsImage = Array(offerImageList.size) { ImageView(activity) }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
//            setMargins(activity.resources.getDimension(R.dimen.indicator_item_spacing).toInt(), 0, activity.resources.getDimension(R.dimen.indicator_item_spacing).toInt(), 0)
            rightMargin = activity.resources.getDimension(R.dimen.indicator_item_spacing).toInt()
            leftMargin = activity.resources.getDimension(R.dimen.indicator_item_spacing).toInt()
        }

        dotsImage.forEach {
            it.setImageResource(
                R.drawable.non_active_dot
            )
            binding.slideDotLL.addView(it, params)
        }
        // default first dot selected
        dotsImage[0].setImageResource(R.drawable.active_dot)

        binding.vpSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImage.mapIndexed { index, imageView ->
                    if (position == index) {
                        imageView.setImageResource(
                            R.drawable.active_dot
                        )
                    } else {
                        imageView.setImageResource(R.drawable.non_active_dot)
                    }
                }
                super.onPageSelected(position)
            }
        })

        // Optional: Auto-scroll the ViewPager
        startAutoSlide(offerImageList)
    }

    private fun startAutoSlide(imageList: ArrayList<ProductOffer>) {
        val viewPager = binding.vpSlider
        val handler = android.os.Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (viewPager.currentItem == imageList.size - 1) {
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
                handler.postDelayed(
                    this,
                    3000
                ) // Change slide interval here (in milliseconds)
            }
        }
        handler.postDelayed(runnable, 3000) // Change initial delay here (in milliseconds)
    }

    private fun getExclusiveOffer() {
        exclusiveProductList.clear()
        val documentReference = FirebaseDB().getCollectionReferenceForExclusiveOffer()
        documentReference.addSnapshotListener { value, error ->
            val data = value?.toObjects(Products::class.java)
            Needle.onBackgroundThread().execute(object : UiRelatedTask<Void?>() {
                override fun doWork(): Void? {
                    if (data != null) exclusiveProductList.addAll(data)
                    Log.e("doWork: ", "exclusiveProductList => $exclusiveProductList")
                    return null
                }

                override fun thenDoUiRelatedWork(result: Void?) {
                    if (exclusiveProductList.size == 0) {
                        binding.rcvExclusiveOffer.beGone()
                    } else {
                        setExclusiveAdapter()
                    }
                }
            })
        }
    }

    private fun setExclusiveAdapter() {
        val exclusiveProductAdapter =
            ExclusiveProductAdapter(activity, exclusiveProductList,
                object : ExclusiveProductAdapter.OnItemClickListener {
                    override fun onClicked(position: Int) {
                        val item = exclusiveProductList[position]
                        addProductInBasket(item)
                    }
                })
        binding.rcvExclusiveOffer.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                AdaptiveSpacingItemDecoration(
                    activity.resources.getDimension(R.dimen.best_selling_item_spacing).toInt(),
                    false
                )
            )
            setHasFixedSize(true)
            adapter = exclusiveProductAdapter
        }
        binding.rcvExclusiveOffer.beVisible()
    }

    private fun getBestSelling() {
        bestSellingProductList.clear()
        val documentReference = FirebaseDB().getCollectionReferenceForBestSelling()
        documentReference.addSnapshotListener { value, error ->
            val data = value?.toObjects(Products::class.java)
            Needle.onBackgroundThread().execute(object : UiRelatedTask<Void?>() {
                override fun doWork(): Void? {
                    if (data != null) bestSellingProductList.addAll(data)
                    Log.e("doWork: ", "bestSellingProductList => $bestSellingProductList")
                    return null
                }

                override fun thenDoUiRelatedWork(result: Void?) {
                    if (bestSellingProductList.size == 0) {
                        binding.isEmptyList.beVisible()
                        binding.isFilesLoading.beGone()
                        binding.rcvBestSelling.beGone()
                    } else {
                        setBestSellingAdapter()
                    }
                }
            })
        }
    }

    private fun setBestSellingAdapter() {
        val bestSellingProductAdapter =
            BestSellingProductAdapter(activity, bestSellingProductList,
                object : BestSellingProductAdapter.OnItemClickListener {
                    override fun onClicked(position: Int) {
                        val item = bestSellingProductList[position]
                        addProductInBasket(item)
                    }
                })
        binding.rcvBestSelling.apply {
            layoutManager =
                GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(
                AdaptiveSpacingItemDecoration(
                    activity.resources.getDimension(R.dimen.best_selling_item_spacing).toInt(), true
                )
            )
            setHasFixedSize(true)
            adapter = bestSellingProductAdapter
        }
        binding.isEmptyList.beGone()
        binding.isFilesLoading.beGone()
        binding.rcvBestSelling.beVisible()
    }

    @SuppressLint("HardwareIds")
    private fun addProductInBasket(item: Products) {
        val deviceId: String =
            Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
        val documentReference =
            FirebaseDB().getCollectionReferenceForBasket(deviceId)
        val lastOrderId =
            appPreference.getPreference(AppPersistence.keys.LAST_ORDER_ID_NUMBER) as Int
        val orderIdNumber = lastOrderId + 1
        val customId = "order_$orderIdNumber"
        val product_order_quantity = "1"
        val basketProduct = CartProductsData(
            customId,
            item.product_image,
            item.product_name,
            item.product_details,
            item.product_benefits,
            item.product_review,
            item.product_quantity,
            product_order_quantity,
            item.product_weight,
            item.product_price,
            item.product_price
        )
        documentReference.document(customId).set(basketProduct)
            .addOnSuccessListener {
                // Data uploaded successfully
                // You can add any success handling logic here
                appPreference.setPreference(AppPersistence.keys.LAST_ORDER_ID_NUMBER, orderIdNumber)
                Utils().showToast(activity, getString(R.string.product_add_in_basket))
                listRefresh = true

            }
            .addOnFailureListener { e ->
                // Handle the failure
                Log.e("Firestore", "Error uploading item with custom ID $customId", e)
                Utils().showToast(activity, getString(R.string.product_add_failed_in_basket))
            }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

