<p><img src="https://img.shields.io/badge/-ModLoader:%20Forge-lightgrey" width="111" height="20" />&nbsp;<img src="https://img.shields.io/badge/-Minecraft%201.18.2-green" alt="" width="101" height="20" />&nbsp;<img src="https://img.shields.io/badge/-More%20versions%20&amp;%20optimizations%20are%20work%20in%20progress-informational" width="295" height="20" />&nbsp;<img src="https://img.shields.io/badge/-Client%20Mod%20-critical" width="69" height="20" />&nbsp;</p>
<h5><strong>Are you using a large modpack? Do you also experience insanely high launch times when using it?<br /> Then my mod "Lightspeed" might be just what you are looking for.</strong></h5>
<p>&nbsp;</p>
<p><strong>🥳Thank you for 1000 Downloads - That's incredible for this short amount of time. More optimizations will come soon :)</strong></p>
<p><strong>📢&nbsp;Offer: </strong>If you are still facing high launch times I encourage you to post a link to your modpack - I will then analyze it and provide further optimizations.</p>
<p>📰&nbsp;<strong>News:&nbsp;</strong>In near future I'll test this mod with the most downloaded modpacks on curseforge in order to ensure overall compatibility and performance.</p>
<p>👀 <strong><b>Request:&nbsp;</b></strong>If you notice any issues please report them (on <a href="https://github.com/CCr4ft3r/lightspeed/issues">GitHub</a>) -&nbsp;no improvement is worth having any crashes or incompatibility.</p>
<p>&nbsp;</p>
<h4><b>💡<strong>About</strong></b></h4>
<p>Lightspeed is a launch optimization mod, that aims to significantly reduce minecrafts launch time when using (heavy) modpacks. <br />At the moment Lightspeed decreases minecraft's <strong>launch time by approx. 48% - 62%</strong></p>
<p>This mod <strong>is still work in progress</strong> and all optimizations will be added bit by bit to ensure a stable and slowly getting better experience.</p>
<p>To let you track the launch time improvement for every update Lightspeed displays the launch time at the title screen (look at the Images tab).</p>
<p style="text-align: left;">&nbsp;</p>
<h4>📜&nbsp;<strong>Motivation</strong></h4>
<p style="text-align: left;">I'm using a large modpack myself that includes 273 mods (Minecraft 1.18.2). Minecraft takes half an eternity to start - even though I have a solid pc setup.</p>
<p style="text-align: left;">An up-to-date computer takes <strong>7 minutes</strong>, a setup of 2015 approx. 15 min and a notebook of 2018 even needs more than 20 min to launch minecraft.</p>
<p style="text-align: left;">That's not really good.</p>
<p style="text-align: left;">&nbsp;</p>
<h4><span class="emoji-icon-wrap">🔭</span><b> <strong>Roadmap</strong></b></h4>
<p>At the moment Lightspeed decreases minecraft's launch time by approx. 48% - 62%. Every future update aims to improve it at least by 10%.<br />My final goal is that Lightspeed make your launch <strong>3 to 4 times faster</strong> than without using it. <br />Meaning an unoptimized launch of 7 minutes should <strong>be reduced to 2 minutes</strong>, 15 minutes to 4 minutes and so on. <br /><strong>If you like this mod,&nbsp;</strong>I would be happy if you post a comment with your initial launch time and the time achieved by using Lightspeed.</p>
<p>&nbsp;</p>
<h4 class="LC20lb MBeuO DKV0Md">🧭<strong>Maxims (Guiding principles)</strong></h4>
<p style="text-align: left;">To ensure that my goal (improved loading times for everyone) is actually achieved, Lightspeed follows or should follow a few guiding principles:</p>
<ul>
<li style="text-align: left;"><strong>Efficiency over multithreading</strong>:&nbsp;Not everyone has a gaming cpu with 12, 16 or even 20 cores. Therefore, Lightspeed relies on caches and other measures to minimize operations wherever possible.</li>
<li style="text-align: left;"><strong>Minimally invasive</strong>: Even though Lightspeed is changing existing source code, it's not an completely overhaul or rearrangement of it. Every adjustment&nbsp;takes place with utmost care.</li>
<li style="text-align: left;"><strong>(Ideally) no incompatibilities</strong>: It's my goal that Lightspeed is and will be compatible with almost all mods out there - if you experience any crashes, weird behaviours or other issues please create an issue on GitHub.</li>
</ul>
<p>&nbsp;</p>
<h4 class="LC20lb MBeuO DKV0Md"><strong>⚗️How it works</strong></h4>
<p>Minecraft and Forge are using some inefficient or redundant ways to load, hold and process data. I found some of these.</p>
<p>I implemented an alternative algorithm per inefficiency and then observed the startup again to verify that the change resulted in the expected improvement.</p>
<p>Lightspeed's optimizations will mainly focus on:</p>
<ul>
<li>reducing file system operations (especially read access)</li>
<li>parallelizing suitable processes</li>
<li>caching computed results and data</li>
<li>improved data structures</li>
</ul>
<p>&nbsp;</p>
<h4 class="LC20lb MBeuO DKV0Md"><strong>🖥️ Test-Setup&nbsp;</strong><strong>&amp; Compatibilities</strong></h4>
<p>The following section contains all mods that I'm using in my modpack. Which means that all of these mods should be compatible with Lightspeed.&nbsp;</p>
<div class="spoiler">
<p>AbsentByDesign AdditionalAdditions AdditionalLanterns AdditionalLights Alcocraft Alexsmobs AmbientAdditions AmbientSounds Appetite AppleSkin AppollosAdditionalStructures Aquaculture Architects Architectury Artifacts AutoRegLib AwesomeDungeonEnd AwesomeDungeonNether AwesomeDungeonOcean Awesomedungeon BadMobs Balm Beautify Bettas BetterAdvancements BetterAnimalsPlus BetterBiomeBlend BetterFpsDist BetterModsButton BeyondEarth BiomesO'Plenty BlockCarpentry BlueSkies Bookshelf BotanyPots BottleYourXp BottledAir Breezy BuildersAddition BuildersDelight Camera Canary Chipped Chiseled Citadel ClothConfig CofhCore ColdsStructures Collective CombatRoll Configured ConnectedTexturesMod CookingForBlockheads CornDelight Create CreativeCore CreaturesAndBeasts CrittersAndCompanions Croptopia CulturalDelights Curios DecorationDelightMod DecorativeBlocks DeeperInTheCaves Delightful Diet DimDungeons DisplayCase DoggyTalents DomesticationInnovation DomumOrnamentum DrawerFps Duckies DungeonCrawl DungeonsArise DynamicSurroundings Ecologics EntityCollisionFpsFix EntityCulling ExlineFurniture ExoticBirds Expandability ExpandedCaves ExtendedLights FallingLeaves FantasyFurniture FarmersDelight FastLeafDecay FastWorkbench FeatureNbtDeadlockBeGone FerriteCore FinsAndTails FixMySpawnR Floralis Flowery FloweryCore Flywheel FoodEnhancements ForgottenBiomes FramedBlocks Furnish Geckolib Gemsnjewels GoProne Goodall GrapplingHookMod Gravestone GuardVillagers GuiClock HappyHolidays Hedgehog HopoBetterUnderwaterRuins Hyle ImmersiveEngineering ImmversiveFX InControl Incendium IndustrialForegoing InfinityButtons IntegratedStructuresAndDungeons Jade Jeed Jei JeiIntegration JourneyMap Kiwi KnightQuest Koremods KotlinForForge Krypton LazyDfu LibraryFerret LightMeals Lightspeed MacawsBridges MacawsBridgesBOP MacawsDoors MacawsFences MacawsFencesBOP MacawsFurniture MacawsLights MacawsPaths MacawsRoofs MacawsRoofsBOP MacawsTrapdoors MacawsWindows Mantle ManyIdeasCore ManyIdeasDoors Mapperbase McjtyLib MmLib ModernXl MonkeMadness MoogsVoyagerStructures MoreBeautifulPlates MoreStorageDrawers MoreVillagers MouseTweaks MrCrayfish'sFurniture Multibeds NamelessTrinkets Naturalist NaturesCompass Nekoration Nightlights NockEnoughArrows NotJustSandwich Notes Observable ObsidianBoat OhTheBiomesYou'llGo Optifine OutOfSight Pam'sHarvestcraftCrops Pam'sHarvestcraftFoodCore Pam'sHarvestcraftFoodExtended Pam'sHarvestcraftTrees Paraglider Patchouli Phireworks PickUpNotifier Placebo PlayerAnimator PresenceFootsteps PrettyBeaches PrimalReservation ProjectVibrantJourneys PuzzlesLib Pyrotastic Quark QuickPlant Reblured Rechiseled RecipesLibrary RefinedStorage Relics Reliquary RepurposedStructures RexsAdditionalStructures RfToolsBase RoughTweaks Searchlight SecretRooms SecurityCraft Selene ShetiphianCore Shrines Signpost SimpleDivingGear SimplyHouses SimplyLight Sit SmarterFarmers SmoothChunk SnowRealMagic SnowUnderTrees SnowySpirit SophisticatedBackpacks SophisticatedCore SophisticatedStorage SoundPhysics SpiceOfLife SpyglassImprovements Starlight StorageDrawers Structory StructureGel StylishEffects SuperMartijn642ConfigLib SuperMartijn642CoreLib Supplementaries SushiGoCrafting SushiMod TerraBlender Terralith TheAbyss2 TheVeggieWay ThermalCultivation ThermalExpansion ThermalFoundation ThermalInnovation ThermalIntegration ThermalLocomotion Titanium ToughAsNails TownsAndTowers Trapcraft UTeamCore UnionLib Unstructured UntamedWilds UnusualDrill UnusualEnd UsefulBackpacks VanillaDegus Villagernames Waddles Waystones WildBackport Wilds XercaMod Xnet XtraArrows YungsApi YungsBetterDesertTemples YungsBetterDungeons YungsBetterMineshafts YungsBetterStrongholds YungsBetterWitchHuts mOREs</p>
</div>
<p>&nbsp;</p>
<h4 class="LC20lb MBeuO DKV0Md">❗<b><strong>Important Notes</strong></b></h4>
<ul style="text-align: left;">
<li>The optimizations will only show an effect if you are using several or a lot of mods.</li>
<li><del>Some future optimizations may directly tweak other mods.&nbsp;</del> In favor of overall compatibility mod-specific optimizations will only change the code of minecraft instead of the actual mod code.</li>
<li>It will probably not be possible to reduce the launch time down or near to vanilla level (about 40 seconds), because in spite of optimizations there is more to do than not using any mods - But I will try my best to retract that statement at some point.</li>
</ul>
<p style="text-align: left;">&nbsp;</p>
<h4 style="text-align: left;"><b>📝<strong>FAQ</strong></b></h4>
<div class="spoiler" style="text-align: left;">
<p><strong><b>Q: Can I use Lightspeed in my modpack?</b></strong></p>
<p><span style="font-weight: 400;">A: Yes -&nbsp; Feel free to include Lightspeed into your modpack - Remember to give credit and don't claim Lightspeed as your own creation.</span></p>
<p>&nbsp;</p>
<p><strong><b>Q: Which Minecraft versions are supported?</b></strong></p>
<p><span style="font-weight: 400;">A: Lightspeed is currently available for </span><b>1.18.2. Other version will hopefully come soon - feel free to request a specific version.</b></p>
<p>&nbsp;</p>
<p><strong>Q: Are there any known incompatibilities with other mods?</strong></p>
<p>A: Not yet - most mods should be compatible with Lightspeed. If you are facing any problems when using Lightspeed please create an issue on GitHub.</p>
<p>&nbsp;</p>
<p><strong><b>Q: I'm still facing high launch times. Why?</b></strong></p>
<p><span style="font-weight: 400;">A: This mod is still work in progress - look at the Roadmap section for more info. Maybe some mods you are using contain inefficient algorithms. Feel free to create an issue on GitHub with your mod list. I will try to profile and to improve it. Another reason might be your&nbsp;maximum memory allocation (via -XMX) - if this value is too low, minecraft will try to free memory more often and this will hurt the launch time.</span></p>
</div>
<p style="text-align: left;">&nbsp;</p>
<h4 style="text-align: left;"><b>🌎 <strong>Links</strong></b></h4>
<p style="text-align: left;"><a href="https://github.com/CCr4ft3r/lightspeed/issues"><b>Report issues and request features</b></a></p>