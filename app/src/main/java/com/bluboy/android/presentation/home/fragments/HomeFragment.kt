package com.bluboy.android.presentation.home.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bluboy.android.R
import com.bluboy.android.data.models.*
import com.bluboy.android.databinding.FragmentHomeBinding
import com.bluboy.android.presentation.core.BaseFragment
import com.bluboy.android.presentation.home.HomeActivity
import com.bluboy.android.presentation.home.HomeViewModel
import com.bluboy.android.presentation.home.adapter.*
import com.bluboy.android.presentation.utility.*
import com.bluboy.android.presentation.utility.bannerLilbrary.BaseBannerAdapter
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.util.*


class HomeFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var homeAdapter: HomeParentAdapter
    private lateinit var homeAllGameAdapter: HomeAllGameAdapter
    private lateinit var adapterCarousel: HomeGameCarouselAdapter
    private lateinit var homePopularAdapter: PopularGameAdapter
    private var arrayGames = ArrayList<GamesData>()
    private var arrayCategory = ArrayList<CategoryData>()
    private var arrayPopularGames = ArrayList<TrendingGame>()
    var arrayBanner = ArrayList<Banner>()
    var bannerData = ArrayList<String>()
    private var arrayFeatureGames = ArrayList<GamesData>()
    private var userData: User? = null

    //private var homeImagesPagerAdapter: HomeBannerPagerAdapter? = null
    private var homeFeaturePagerAdapter: HomeFeaturePagerAdapter? = null
    private var page = 1
    private var bannerHandlerBanner = Handler(Looper.getMainLooper())
    private var bannerRunnableBanner: Runnable? = null
    private var bannerCurrentPage = 0
    private var bannerHandlerFeature = Handler(Looper.getMainLooper())
    private var bannerRunnableFeature: Runnable? = null
    private var featureCurrentPage = 0
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        arrayGames.clear()
        arrayCategory.clear()
        homeViewModel.getProfile()
        homeViewModel.getGameList(page)
        homeViewModel.getGameCategory()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun attachObserver() {
        homeViewModel.profileObserver.observe(this, Observer {
            if (it.status == 1) {
                it.user?.let { it1 -> PrefKeys.setUser(it1) }
                userData = it.user
                binding.textViewBonus.text = userData?.userBonusBalance
                HomeActivity.bonusAmt = userData?.userBonusBalance.toString()
                binding.textViewCashBal.text =
                    (userData?.userDepositBalance?.toFloat()!! + userData?.userWinningAmountBalance?.toFloat()!!).toString()

                Glide.with(requireContext())
                    .load(userData?.profilePic)
                    .circleCrop()
                    .thumbnail(Glide.with(requireContext()).load(R.raw.loader))
                    .into(binding.imageViewProfile);

                //  loadCircleImage(binding.imageViewProfile, userData?.profilePic)

                File("uri")

            } else {

            }
        })

        homeViewModel.gamesListObserver.observe(this, Observer {
            binding.mShimmerViewContainer.gone()
            if (it.status == 1) {
                arrayGames.clear()
                arrayGames.addAll(it.data.games)
//                homeAllGameAdapter.notifyDataSetChanged()
                adapterCarousel.notifyDataSetChanged()
            }
        })

        homeViewModel.gameCategoryObserver.observe(this, Observer {
            binding.mShimmerViewContainer?.gone()
            if (it.status == 1) {
                arrayCategory.clear()
                arrayCategory.addAll(it.data)
                homeAdapter.notifyDataSetChanged()
            }
        })

        homeViewModel.featuredGamesListObserver.observe(this, Observer {
            binding.mShimmerViewPaggerFeaturedGame?.gone()
            if (it.status == 1) {
                binding.textViewFeaturedGames?.visible()//visible before
                binding.viewPagerFeaturedGames?.visible()//visible before
                arrayFeatureGames?.clear()
                arrayFeatureGames?.addAll(it.data.games)

                homeFeaturePagerAdapter = HomeFeaturePagerAdapter(
                    requireContext(),
                    arrayFeatureGames
                ) { games, position ->
                    startActivityCustom(
                        IntentHelper.getGameScreenIntent(
                            requireContext(),
                            games
                        )
                    )
                }

                binding.viewPagerFeaturedGames.adapter = homeFeaturePagerAdapter
                binding.viewPagerFeaturedGames.addOnPageChangeListener(object :
                    ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {

                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                    }

                    override fun onPageSelected(position: Int) {
                    }
                })
                binding.dotsIndicatorFeaturedGames.setViewPager(binding.viewPagerFeaturedGames)
                startAutoScrollFeature()
            } else {
                binding.textViewFeaturedGames.gone()
                binding.viewPagerFeaturedGames.gone()
            }
        })

        // Popular/Trending Games
        homeViewModel.trendingGamesObserver.observe(this, Observer {
            binding.mShimmerViewPaggerPopularGame.gone()
            if (it.status == 1) {
                binding.textViewPopularGame.visible()//visible before
                binding.horizontalScrollViewTrending.visible()//visible before
                arrayPopularGames.clear()
                arrayPopularGames.addAll(it.data.games)
                if (arrayPopularGames.size == 0) {
                    binding.textViewPopularGame.gone()
                }
                homePopularAdapter.notifyDataSetChanged()
            } else {
                binding.textViewPopularGame.gone()
                binding.horizontalScrollViewTrending.gone()

            }
        })

        // Banner
        homeViewModel.bannerObserver.observe(this, Observer {
            binding.mShimmerViewPaggerBanner.gone()
            if (it.status == 1) {
                // BANNER
                binding.bannerViewpager.visible()
                arrayBanner.clear()
                bannerData.clear()

                arrayBanner.addAll(it.data.banners)
                arrayBanner.forEach { banner ->
                    bannerData.add(banner.bannerImage)
                }

                //Banner
                val webBannerAdapter = BaseBannerAdapter(requireContext(), bannerData)
                webBannerAdapter.setOnBannerItemClickListener { position ->
                    if (arrayBanner[position].bannerTargetUrl.isNotBlank()) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse(arrayBanner[position].bannerTargetUrl)
                            )
                        )
                    }
                }

                binding.bannerViewpager.setAdapter(webBannerAdapter);

            } else {
                binding.bannerViewpager.gone()
            }
        })

    }

    private fun init() {
        attachObserver()

        arrayPopularGames.clear()
        arrayBanner.clear()
        arrayFeatureGames.clear()

        homeViewModel.getFeaturedGames(page)
        homeViewModel.ApiBanner(page)
        homeViewModel.ApiTrendingGames(page)

        binding.imageViewProfile.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getProfileScreenIntent(requireContext()))
        }

        binding.imagViewBluBoy.setSafeOnClickListener {
            (activity as HomeActivity).openDrawer()
        }

          binding.spinBanner.setSafeOnClickListener {
              (activity as HomeActivity).launchBottomNavFragmentByIndex(1)
          }

        binding.imageViewReferEarnHome.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getReferAndEarnFromHomeScreenIntent(requireContext()))
        }

        binding.imageViewNotification.setSafeOnClickListener {
            startActivityCustom(IntentHelper.getNotificationScreenIntent(requireContext()))
        }

        arrayGames.clear()
        arrayCategory.clear()
        arrayPopularGames.clear()

        var linearLayoutManagerPopular = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewPopularGame.layoutManager = linearLayoutManagerPopular
        homePopularAdapter =
            PopularGameAdapter(requireContext(), arrayPopularGames) { games, position ->
                if (games.gameCommingSoon == "Y") {
                } else {
                    var mgames = GamesData(
                        games.categoryName,
                        games.gameBanner,
                        games.gameDescription,
                        games.gameFeatured,
                        games.gameId,
                        games.game_key,
                        games.gameImage,
                        games.gameName,
                        games.gameOrientation,
                        games.gameStatus,
                        games.gameType,
                        games.gameUrl,
                        games.gameVersion,
                        games.totalOnlineUsers,
                        "N", games.gameTile, games.gameTag
                    )
                    startActivityCustom(IntentHelper.getGameScreenIntent(requireContext(), mgames))
                }

            }
        binding.recyclerViewPopularGame.adapter = homePopularAdapter

        binding.constraintMoreGames.setOnClickListener {
            Handler(Looper.getMainLooper()).post {
                binding.nsScroll.smoothScrollTo(
                    0,
                    binding.dotsIndicatorFeaturedGames.bottom
                )
            }
        }


        var linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewHomeGame.layoutManager = linearLayoutManager
        homeAdapter = HomeParentAdapter(requireContext(), arrayCategory) { games, position ->
            if (games.gameCommingSoon == "Y") {
            } else {
                startActivityCustom(IntentHelper.getGameScreenIntent(requireContext(), games))
            }
        }
        binding.recyclerViewHomeGame.adapter = homeAdapter
        var linearLayoutManager1 = GridLayoutManager(activity, 2)
        binding.recyclerViewAllGame.layoutManager = linearLayoutManager1

        homeAllGameAdapter = HomeAllGameAdapter(requireContext(), arrayGames) { games, position ->
            if (games.gameCommingSoon != "Y") {
                startActivityCustom(IntentHelper.getGameScreenIntent(requireContext(), games))
            }
        }

        val spanCount = 2 // 2 columns
        val spacing =
            Math.round(15 * getResources().getDisplayMetrics().density) //90 // 50px  //Math.round(30 * getResources().getDisplayMetrics().density)
        val includeEdge = true
        binding.recyclerViewAllGame.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerViewAllGame.adapter = homeAllGameAdapter


        //carrousel games
        adapterCarousel = HomeGameCarouselAdapter(requireContext(), arrayGames) { games, position ->
            if (games.gameCommingSoon != "Y") {
                startActivityCustom(IntentHelper.getGameScreenIntent(requireContext(), games))
            }
        }

        binding.recyclerCarousel.apply {
            this.adapter = adapterCarousel
            set3DItem(true)
            setAlpha(true)
        }
    }

    private fun startAutoScrollFeature() {
        if (bannerRunnableFeature == null) {
            bannerRunnableFeature = Runnable {
                if (activity != null && activity?.isFinishing == false) {
                    if (featureCurrentPage == arrayFeatureGames.size) {
                        featureCurrentPage = 0
                    }
                    if (activity?.isFinishing == false) {
                        binding.viewPagerFeaturedGames.setCurrentItem(featureCurrentPage++, true)
                    }
                }
            }

            val size = arrayFeatureGames.size ?: 1
            Logger.d("Banner image count : $size")
            if (size > 0) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        bannerHandlerFeature.post(bannerRunnableFeature!!)
                    }
                }, 2000L, (size * 1000).toLong())
            }
        }
    }

    private fun stopBannerScrollBanner() {
        if (bannerHandlerBanner != null && bannerRunnableBanner != null) {
            bannerHandlerBanner.removeCallbacks(bannerRunnableBanner!!)
        }
    }

    private fun stopBannerScrollFeature() {
        if (bannerHandlerFeature != null && bannerRunnableFeature != null) {
            bannerHandlerFeature.removeCallbacks(bannerRunnableFeature!!)
        }
    }

    override fun onPause() {
        stopBannerScrollFeature()
        super.onPause()
    }
}