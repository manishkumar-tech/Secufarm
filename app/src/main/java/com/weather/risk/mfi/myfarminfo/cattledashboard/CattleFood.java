package com.weather.risk.mfi.myfarminfo.cattledashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.databinding.CattlefoodBinding;

import static com.weather.risk.mfi.myfarminfo.cattledashboard.CattleDashboards.SelectedLifeStageID;
import static com.weather.risk.mfi.myfarminfo.cattledashboard.CattleDashboards.SelectedLifeStageName;
import static com.weather.risk.mfi.myfarminfo.utils.Utility.getDecimal;

public class CattleFood extends AppCompatActivity {

    CattlefoodBinding binding;

    double[] Productfor_CalfHeifer;
    double[] Productfor_DryPregnant;
    double[] Productfor_MilkingPregMilking;
    double val = 0.0;
    //Calf
    double[] Calf_20kg;
    double[] Calf_25kg;
    double[] Calf_35kg;
    double[] Calf_50kg;
    double[] Calf_75kg;
    //Heifer
    double[] Heifer_100kg;
    double[] Heifer_150kg;
    double[] Heifer_200kg;
    double[] Heifer_225kg;
    double[] Heifer_250kg;
    double[] Heifer_300kg;
    //DryPreg
    double[] DryPreg_300kg;
    double[] DryPreg_350kg;
    double[] DryPreg_400kg;
    double[] DryPreg_450kg;
    double[] DryPreg_500kg;
    //Milking
    double[] MilkingPreg_2p5ltrs;
    double[] MilkingPreg_5ltrs;
    double[] MilkingPreg_7p5ltrs;
    double[] MilkingPreg_10ltrs;
    double[] MilkingPreg_12p5ltrs;

    double[] AnimalValue;
    double[] AnimalValueMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cattlefood);


//        SelectedLifeStageName = "Calf";
//        SelectedLifeStageName = "Heifer";
//        SelectedLifeStageName = "Pregnant Heifer";
//        SelectedLifeStageName = "Milking";
        binding.txtAnimalTitle.setText(SelectedLifeStageName);


        if (SelectedLifeStageName.equalsIgnoreCase("Milking") ||
                SelectedLifeStageName.equalsIgnoreCase("Pregnant Milking")) {
            binding.llBodyweight.setVisibility(View.GONE);
            binding.llCowBuffaloMilk.setVisibility(View.VISIBLE);
            binding.txtBodysize.setText(getResources().getString(R.string.Milklt));
            binding.txtValueUnit.setText("lt");
        } else {
            binding.llBodyweight.setVisibility(View.VISIBLE);
            binding.llCowBuffaloMilk.setVisibility(View.GONE);
            binding.txtBodysize.setText(getResources().getString(R.string.Weightkg));
            binding.txtValueUnit.setText("kg");
        }

        setFoodProductCalfHeifer();
        setFoodProductMilking();
        setFoodDryPregnant();

        setBodyWeightCalf();
        setBodyWeightHeifer();
        setBodyWeightDryPregnant();
        setBodyWeightMilkingPregnant();

        binding.txtBodySizeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != null && s.length() > 0) {
                    double nValue = Double.parseDouble(s.toString());
                    if (nValue > 0) {
                        setValueinKG(nValue);
                    } else {
                        setZeroCattleFood();
                    }
                } else {
                    setZeroCattleFood();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {


                }
            }
        });

        binding.imgvwBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (SelectedLifeStageName.equalsIgnoreCase("Heifer")) {
            setVisible(1);
        } else {
            setVisible(0);
        }

    }

    public void setFoodProductCalfHeifer() {
        try {
            double Gainproduct = 50.0 / 100.0;
            double Gainbyproduct = 17.0 / 100.0;
            double OilCake = 30.0 / 100.0;
            double Sail = 1.0 / 100.0;
            double MineralMixture = 2.0 / 100.0;
            double[] CalfHeifer = {getDecimal(Gainproduct), getDecimal(Gainbyproduct), getDecimal(OilCake), getDecimal(Sail), getDecimal(MineralMixture)};
            Productfor_CalfHeifer = new double[5];
            Productfor_CalfHeifer = CalfHeifer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setFoodProductMilking() {
        try {
            double Gainproduct = 38.0 / 100;
            double Gainbyproduct = 30.0 / 100;
            double OilCake = 30.0 / 100;
            double Sail = 1.0 / 100;
            double MineralMixture = 1.0 / 100;
            double[] MilkingPregMilking = {getDecimal(Gainproduct), getDecimal(Gainbyproduct), getDecimal(OilCake), getDecimal(Sail), getDecimal(MineralMixture)};
            Productfor_MilkingPregMilking = new double[5];
            Productfor_MilkingPregMilking = MilkingPregMilking;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setFoodDryPregnant() {
        try {
            double Gainproduct = 58.0 / 100;
            double Gainbyproduct = 30.0 / 100;
            double OilCake = 10.0 / 100;
            double Sail = 1.0 / 100;
            double MineralMixture = 1.0 / 100;
            double[] DryPregnant = {getDecimal(Gainproduct), getDecimal(Gainbyproduct), getDecimal(OilCake), getDecimal(Sail), getDecimal(MineralMixture)};
            Productfor_DryPregnant = new double[5];
            Productfor_DryPregnant = DryPregnant;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBodyWeightCalf() {
        try {
            Calf_20kg = new double[5];
            val = 0.10;
            double[] Calf20 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Calf_20kg = Calf20;

            Calf_25kg = new double[5];
            val = 0.15;
            double[] Calf25 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Calf_25kg = Calf25;

            Calf_35kg = new double[5];
            val = 0.30;
            double[] Calf35 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Calf_35kg = Calf35;

            Calf_50kg = new double[5];
            val = 0.50;
            double[] Calf50 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Calf_50kg = Calf50;

            Calf_75kg = new double[5];
            val = 0.55;
            double[] Calf75 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Calf_75kg = Calf75;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBodyWeightHeifer() {
        try {
            Heifer_100kg = new double[5];
            val = 1.25;
            double[] Heifer_100 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Heifer_100kg = Heifer_100;

            Heifer_150kg = new double[5];
            val = 1.50;
            double[] Heifer_150 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Heifer_150kg = Heifer_150;

            Heifer_200kg = new double[5];
            val = 2.0;
            double[] Heifer_200 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Heifer_200kg = Heifer_200;

            Heifer_225kg = new double[5];
            val = 2.25;
            double[] Heifer_225 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Heifer_225kg = Heifer_225;

            Heifer_250kg = new double[5];
            val = 2.50;
            double[] Heifer_250 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Heifer_250kg = Heifer_250;

            Heifer_300kg = new double[5];
            val = 3.00;
            double[] Heifer_300 = {getDecimal(Productfor_CalfHeifer[0] * val), getDecimal(Productfor_CalfHeifer[1] * val), getDecimal(Productfor_CalfHeifer[2] * val), getDecimal(Productfor_CalfHeifer[3] * val), getDecimal(Productfor_CalfHeifer[4] * val)};
            Heifer_300kg = Heifer_300;


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBodyWeightDryPregnant() {
        try {
            DryPreg_300kg = new double[5];
            val = 3.00;
            double[] DryPreg_300 = {getDecimal(Productfor_DryPregnant[0] * val), getDecimal(Productfor_DryPregnant[1] * val), getDecimal(Productfor_DryPregnant[2] * val), getDecimal(Productfor_DryPregnant[3] * val), getDecimal(Productfor_DryPregnant[4] * val)};
            DryPreg_300kg = DryPreg_300;

            DryPreg_350kg = new double[5];
            val = 3.50;
            double[] DryPreg_350 = {getDecimal(Productfor_DryPregnant[0] * val), getDecimal(Productfor_DryPregnant[1] * val), getDecimal(Productfor_DryPregnant[2] * val), getDecimal(Productfor_DryPregnant[3] * val), getDecimal(Productfor_DryPregnant[4] * val)};
            DryPreg_300kg = DryPreg_300;

            DryPreg_400kg = new double[5];
            val = 4.00;
            double[] DryPreg_400 = {getDecimal(Productfor_DryPregnant[0] * val), getDecimal(Productfor_DryPregnant[1] * val), getDecimal(Productfor_DryPregnant[2] * val), getDecimal(Productfor_DryPregnant[3] * val), getDecimal(Productfor_DryPregnant[4] * val)};
            DryPreg_400kg = DryPreg_400;

            DryPreg_450kg = new double[5];
            val = 4.50;
            double[] DryPreg_450 = {getDecimal(Productfor_DryPregnant[0] * val), getDecimal(Productfor_DryPregnant[1] * val), getDecimal(Productfor_DryPregnant[2] * val), getDecimal(Productfor_DryPregnant[3] * val), getDecimal(Productfor_DryPregnant[4] * val)};
            DryPreg_450kg = DryPreg_450;

            DryPreg_500kg = new double[5];
            val = 5.00;
            double[] DryPreg_500 = {getDecimal(Productfor_DryPregnant[0] * val), getDecimal(Productfor_DryPregnant[1] * val), getDecimal(Productfor_DryPregnant[2] * val), getDecimal(Productfor_DryPregnant[3] * val), getDecimal(Productfor_DryPregnant[4] * val)};
            DryPreg_500kg = DryPreg_500;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBodyWeightMilkingPregnant() {
        try {
            MilkingPreg_2p5ltrs = new double[5];
            val = 1;
            double[] MilkingPreg_2p5 = {getDecimal(Productfor_MilkingPregMilking[0] * val), getDecimal(Productfor_MilkingPregMilking[1] * val), getDecimal(Productfor_MilkingPregMilking[2] * val), getDecimal(Productfor_MilkingPregMilking[3] * val), getDecimal(Productfor_MilkingPregMilking[4] * val)};
            MilkingPreg_2p5ltrs = MilkingPreg_2p5;

            MilkingPreg_5ltrs = new double[5];
            val = 2;
            double[] MilkingPreg_5 = {getDecimal(Productfor_MilkingPregMilking[0] * val), getDecimal(Productfor_MilkingPregMilking[1] * val), getDecimal(Productfor_MilkingPregMilking[2] * val), getDecimal(Productfor_MilkingPregMilking[3] * val), getDecimal(Productfor_MilkingPregMilking[4] * val)};
            MilkingPreg_5ltrs = MilkingPreg_5;

            MilkingPreg_7p5ltrs = new double[5];
            val = 3;
            double[] MilkingPreg_7p5 = {getDecimal(Productfor_MilkingPregMilking[0] * val), getDecimal(Productfor_MilkingPregMilking[1] * val), getDecimal(Productfor_MilkingPregMilking[2] * val), getDecimal(Productfor_MilkingPregMilking[3] * val), getDecimal(Productfor_MilkingPregMilking[4] * val)};
            MilkingPreg_7p5ltrs = MilkingPreg_7p5;

            MilkingPreg_10ltrs = new double[5];
            val = 4;
            double[] MilkingPreg_10 = {getDecimal(Productfor_MilkingPregMilking[0] * val), getDecimal(Productfor_MilkingPregMilking[1] * val), getDecimal(Productfor_MilkingPregMilking[2] * val), getDecimal(Productfor_MilkingPregMilking[3] * val), getDecimal(Productfor_MilkingPregMilking[4] * val)};
            MilkingPreg_10ltrs = MilkingPreg_10;

            MilkingPreg_12p5ltrs = new double[5];
            val = 5;
            double[] MilkingPreg_12p5 = {getDecimal(Productfor_MilkingPregMilking[0] * val), getDecimal(Productfor_MilkingPregMilking[1] * val), getDecimal(Productfor_MilkingPregMilking[2] * val), getDecimal(Productfor_MilkingPregMilking[3] * val), getDecimal(Productfor_MilkingPregMilking[4] * val)};
            MilkingPreg_12p5ltrs = MilkingPreg_12p5;


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void setValueinKG(double Value) {
        try {
            AnimalValue = new double[5];
            AnimalValueMax = new double[5];
            binding.txtAccordingtoourrecords.setVisibility(View.GONE);

            if (SelectedLifeStageName.equalsIgnoreCase("Calf")) {
                //Calf
                setVisible(0);
                if (Value > 0.0 && Value <= 20.0) {
                    AnimalValue = Calf_20kg;
                } else if (Value > 20.0 && Value <= 25.0) {
                    AnimalValue = Calf_25kg;
                } else if (Value > 25.0 && Value <= 35.0) {
                    AnimalValue = Calf_35kg;
                } else if (Value > 35.0 && Value <= 50.0) {
                    AnimalValue = Calf_50kg;
                } else if (Value > 50.0 && Value <= 90.0) {
//                } else if (Value > 50.0 && Value <= 75.0) {
                    AnimalValue = Calf_75kg;
                } else {
                    binding.txtAccordingtoourrecords.setVisibility(View.VISIBLE);
                    if (Value <= 100.0) {
                        AnimalValue = Heifer_100kg;
                        AnimalValueMax = Heifer_150kg;
                    } else if (Value > 100.0 && Value <= 150.0) {
                        AnimalValue = Heifer_150kg;
                        AnimalValueMax = Heifer_200kg;
                    } else if (Value > 150.0 && Value <= 200.0) {
                        AnimalValue = Heifer_200kg;
                        AnimalValueMax = Heifer_250kg;
                    } else if (Value > 200.0 && Value <= 250.0) {
                        AnimalValue = Heifer_250kg;
                        AnimalValueMax = Heifer_300kg;
                    } else if (Value > 250.0 && Value <= 300.0) {
                        AnimalValue = Heifer_250kg;
                        AnimalValueMax = Heifer_300kg;
                    }
                    setVisible(1);
                    String GainProductkgmax = String.valueOf(AnimalValueMax[0]) + " kg";
                    binding.txtGainProductkgmax.setText(GainProductkgmax);
                    String GainbyProductkgmax = String.valueOf(AnimalValueMax[1]) + " kg";
                    binding.txtGainbyProductkgMax.setText(GainbyProductkgmax);
                    String OilCakekgmax = String.valueOf(AnimalValueMax[2]) + " kg";
                    binding.txtOilCakekgMax.setText(OilCakekgmax);
                    String Saltkgmax = String.valueOf(AnimalValueMax[3]) + " kg";
                    binding.txtSaltkgMax.setText(Saltkgmax);
                    String MineralMixturekgmax = String.valueOf(AnimalValueMax[4]) + " kg";
                    binding.txtMineralMixturekgMax.setText(MineralMixturekgmax);
                }

            } else if (SelectedLifeStageName.equalsIgnoreCase("Heifer")) {
                //Heifer
                setVisible(1);
                if (Value > 90.0 && Value <= 100.0) {
                    AnimalValue = Heifer_100kg;
                    AnimalValueMax = Heifer_150kg;
                } else if (Value > 100.0 && Value <= 150.0) {
                    AnimalValue = Heifer_150kg;
                    AnimalValueMax = Heifer_200kg;
                } else if (Value > 150.0 && Value <= 200.0) {
                    AnimalValue = Heifer_200kg;
                    AnimalValueMax = Heifer_250kg;
                } else if (Value > 200.0 && Value <= 250.0) {
                    AnimalValue = Heifer_250kg;
                    AnimalValueMax = Heifer_300kg;
//                } else if (Value > 250.0 && Value <= 300.0) {
                } else if (Value > 250.0 && Value <= 320.0) {
                    AnimalValue = Heifer_250kg;
                    AnimalValueMax = Heifer_300kg;
                } else {
                    binding.txtAccordingtoourrecords.setVisibility(View.VISIBLE);
                    //Dry Pregnant Dry
                    if (Value > 280.0 && Value <= 300.0) {
                        AnimalValue = DryPreg_300kg;
                    } else if (Value > 300.0 && Value <= 350.0) {
                        AnimalValue = DryPreg_350kg;
                    } else if (Value > 350.0 && Value <= 400.0) {
                        AnimalValue = DryPreg_400kg;
                    } else if (Value > 400.0 && Value <= 450.0) {
                        AnimalValue = DryPreg_450kg;
//                } else if (Value > 450.0 && Value <= 500.0) {
                    } else if (Value > 450.0 && Value <= 520.0) {
                        AnimalValue = DryPreg_500kg;
                    }
                    setVisible(0);
                }


            } else if (SelectedLifeStageName.equalsIgnoreCase("Milking") ||
                    SelectedLifeStageName.equalsIgnoreCase("Pregnant Milking")) {
                if (Value <= 2.5) {
                    AnimalValue = MilkingPreg_2p5ltrs;
                } else if (Value > 2.5 && Value <= 5.0) {
                    AnimalValue = MilkingPreg_5ltrs;
                } else if (Value > 5.0 && Value <= 7.5) {
                    AnimalValue = MilkingPreg_7p5ltrs;
                } else if (Value > 7.5 && Value <= 10.0) {
                    AnimalValue = MilkingPreg_10ltrs;
//                } else if (Value > 10.0 && Value <= 12.5) {
                } else if (Value > 10.0 && Value <= 15.0) {
                    AnimalValue = MilkingPreg_12p5ltrs;
                } else {
                    binding.txtAccordingtoourrecords.setVisibility(View.VISIBLE);
                }

            } else if (SelectedLifeStageName.equalsIgnoreCase("Dry") ||
                    SelectedLifeStageName.equalsIgnoreCase("Pregnant Dry") ||
                    SelectedLifeStageName.equalsIgnoreCase("Pregnant Heifer")) {
                if (Value > 280.0 && Value <= 300.0) {
                    AnimalValue = DryPreg_300kg;
                } else if (Value > 300.0 && Value <= 350.0) {
                    AnimalValue = DryPreg_350kg;
                } else if (Value > 350.0 && Value <= 400.0) {
                    AnimalValue = DryPreg_400kg;
                } else if (Value > 400.0 && Value <= 450.0) {
                    AnimalValue = DryPreg_450kg;
//                } else if (Value > 450.0 && Value <= 500.0) {
                } else if (Value > 450.0 && Value <= 520.0) {
                    AnimalValue = DryPreg_500kg;
                } else {
                    binding.txtAccordingtoourrecords.setVisibility(View.VISIBLE);
                }
            }


            String GainProductkg = String.valueOf(AnimalValue[0]) + " kg";
            binding.txtGainProductkg.setText(GainProductkg);
            String GainbyProductkg = String.valueOf(AnimalValue[1]) + " kg";
            binding.txtGainbyProductkg.setText(GainbyProductkg);
            String OilCakekg = String.valueOf(AnimalValue[2]) + " kg";
            binding.txtOilCakekg.setText(OilCakekg);
            String Saltkg = String.valueOf(AnimalValue[3]) + " kg";
            binding.txtSaltkg.setText(Saltkg);
            String MineralMixturekg = String.valueOf(AnimalValue[4]) + " kg";
            binding.txtMineralMixturekg.setText(MineralMixturekg);

            if (SelectedLifeStageName.equalsIgnoreCase("Heifer") && AnimalValueMax != null) {
                String GainProductkgmax = String.valueOf(AnimalValueMax[0]) + " kg";
                binding.txtGainProductkgmax.setText(GainProductkgmax);
                String GainbyProductkgmax = String.valueOf(AnimalValueMax[1]) + " kg";
                binding.txtGainbyProductkgMax.setText(GainbyProductkgmax);
                String OilCakekgmax = String.valueOf(AnimalValueMax[2]) + " kg";
                binding.txtOilCakekgMax.setText(OilCakekgmax);
                String Saltkgmax = String.valueOf(AnimalValueMax[3]) + " kg";
                binding.txtSaltkgMax.setText(Saltkgmax);
                String MineralMixturekgmax = String.valueOf(AnimalValueMax[4]) + " kg";
                binding.txtMineralMixturekgMax.setText(MineralMixturekgmax);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setZeroCattleFood() {
        binding.txtGainProductkg.setText("0.0 kg");
        binding.txtGainbyProductkg.setText("0.0 kg");
        binding.txtOilCakekg.setText("0.0 kg");
        binding.txtSaltkg.setText("0.0 kg");
        binding.txtMineralMixturekg.setText("0.0 kg");

        binding.txtGainProductkgmax.setText("0.0 kg");
        binding.txtGainbyProductkgMax.setText("0.0 kg");
        binding.txtOilCakekgMax.setText("0.0 kg");
        binding.txtSaltkgMax.setText("0.0 kg");
        binding.txtMineralMixturekgMax.setText("0.0 kg");
    }


    public void setVisible(int flag) {
//        setZeroCattleFood();
        if (flag == 0) {
            binding.txtDoseperdayMax.setVisibility(View.GONE);
            binding.viewDoseperdayMax.setVisibility(View.GONE);
            binding.txtGainProductkgmax.setVisibility(View.GONE);
            binding.viewGainProductkgmax.setVisibility(View.GONE);
            binding.txtGainbyProductkgMax.setVisibility(View.GONE);
            binding.viewGainbyProductkgMax.setVisibility(View.GONE);
            binding.txtOilCakekgMax.setVisibility(View.GONE);
            binding.viewOilCakekgMax.setVisibility(View.GONE);
            binding.txtSaltkgMax.setVisibility(View.GONE);
            binding.viewSaltkgMax.setVisibility(View.GONE);
            binding.txtMineralMixturekgMax.setVisibility(View.GONE);
            binding.viewMineralMixturekgMax.setVisibility(View.GONE);
        } else if (flag == 1) {
            binding.txtDoseperdayMax.setVisibility(View.VISIBLE);
            binding.viewDoseperdayMax.setVisibility(View.VISIBLE);
            binding.txtGainProductkgmax.setVisibility(View.VISIBLE);
            binding.viewGainProductkgmax.setVisibility(View.VISIBLE);
            binding.txtGainbyProductkgMax.setVisibility(View.VISIBLE);
            binding.viewGainbyProductkgMax.setVisibility(View.VISIBLE);
            binding.txtOilCakekgMax.setVisibility(View.VISIBLE);
            binding.viewOilCakekgMax.setVisibility(View.VISIBLE);
            binding.txtSaltkgMax.setVisibility(View.VISIBLE);
            binding.viewSaltkgMax.setVisibility(View.VISIBLE);
            binding.txtMineralMixturekgMax.setVisibility(View.VISIBLE);
            binding.viewMineralMixturekgMax.setVisibility(View.VISIBLE);
        }
    }

}
