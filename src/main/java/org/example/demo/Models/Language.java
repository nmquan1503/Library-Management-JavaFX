package org.example.demo.Models;

public enum Language {
  AFRIKAANS("af", "Afrikaans"),
  ALBANIAN("sq", "Albanian"),
  AMHARIC("am", "Amharic"),
  ARABIC("ar", "Arabic"),
  ARMENIAN("hy", "Armenian"),
  ASSAMESE("as", "Assamese"),
  AYMARA("ay", "Aymara"),
  AZERBAIJANI("az", "Azerbaijani"),
  BAMBARA("bm", "Bambara"),
  BASQUE("eu", "Basque"),
  BELARUSIAN("be", "Belarusian"),
  BENGALI("bn", "Bengali"),
  BHOJPURI("bho", "Bhojpuri"),
  BOSNIAN("bs", "Bosnian"),
  BULGARIAN("bg", "Bulgarian"),
  CATALAN("ca", "Catalan"),
  CEBUANO("ceb", "Cebuano"),
  CHINESE_SIMPLIFIED("zh", "Chinese (Simplified)"),
  CHINESE_TRADITIONAL("zh-TW", "Chinese (Traditional)"),
  CORSICAN("co", "Corsican"),
  CROATIAN("hr", "Croatian"),
  CZECH("cs", "Czech"),
  DANISH("da", "Danish"),
  DHIVEHI("dv", "Dhivehi"),
  DOGRI("doi", "Dogri"),
  DUTCH("nl", "Dutch"),
  ENGLISH("en", "English"),
  ESPERANTO("eo", "Esperanto"),
  ESTONIAN("et", "Estonian"),
  EWE("ee", "Ewe"),
  FILIPINO_TAGALOG("fil", "Filipino (Tagalog)"),
  FINNISH("fi", "Finnish"),
  FRENCH("fr", "French"),
  FRISIAN("fy", "Frisian"),
  GALICIAN("gl", "Galician"),
  GEORGIAN("ka", "Georgian"),
  GERMAN("de", "German"),
  GREEK("el", "Greek"),
  GUARANI("gn", "Guarani"),
  GUJARATI("gu", "Gujarati"),
  HAITIAN_CREOLE("ht", "Haitian Creole"),
  HAUSA("ha", "Hausa"),
  HAWAIIAN("haw", "Hawaiian"),
  HEBREW("he", "Hebrew"),
  HINDI("hi", "Hindi"),
  HMONG("hmn", "Hmong"),
  HUNGARIAN("hu", "Hungarian"),
  ICELANDIC("is", "Icelandic"),
  IGBO("ig", "Igbo"),
  ILOCANO("ilo", "Ilocano"),
  INDONESIAN("id", "Indonesian"),
  IRISH("ga", "Irish"),
  ITALIAN("it", "Italian"),
  JAPANESE("ja", "Japanese"),
  JAVANESE("jv", "Javanese"),
  KANNADA("kn", "Kannada"),
  KAZAKH("kk", "Kazakh"),
  KHMER("km", "Khmer"),
  KINYARWANDA("rw", "Kinyarwanda"),
  KONKANI("gom", "Konkani"),
  KOREAN("ko", "Korean"),
  KRIO("kri", "Krio"),
  KURDISH("ku", "Kurdish"),
  KURDISH_SORANI("ckb", "Kurdish (Sorani)"),
  KYRGYZ("ky", "Kyrgyz"),
  LAO("lo", "Lao"),
  LATIN("la", "Latin"),
  LATVIAN("lv", "Latvian"),
  LINGALA("ln", "Lingala"),
  LITHUANIAN("lt", "Lithuanian"),
  LUGANDA("lg", "Luganda"),
  LUXEMBOURGISH("lb", "Luxembourgish"),
  MACEDONIAN("mk", "Macedonian"),
  MAITHILI("mai", "Maithili"),
  MALAGASY("mg", "Malagasy"),
  MALAY("ms", "Malay"),
  MALAYALAM("ml", "Malayalam"),
  MALTESE("mt", "Maltese"),
  MAORI("mi", "Maori"),
  MARATHI("mr", "Marathi"),
  MEITEILON_MANIPURI("mni-Mtei", "Meiteilon (Manipuri)"),
  MIZO("lus", "Mizo"),
  MONGOLIAN("mn", "Mongolian"),
  MYANMAR_BURMESE("my", "Myanmar (Burmese)"),
  NEPALI("ne", "Nepali"),
  NORWEGIAN("no", "Norwegian"),
  NYANJA_CHICHEWA("ny", "Nyanja (Chichewa)"),
  ODIA_ORIYA("or", "Odia (Oriya)"),
  OROMO("om", "Oromo"),
  PASHTO("ps", "Pashto"),
  PERSIAN("fa", "Persian"),
  POLISH("pl", "Polish"),
  PORTUGUESE("pt", "Portuguese"),
  PUNJABI("pa", "Punjabi"),
  QUECHUA("qu", "Quechua"),
  ROMANIAN("ro", "Romanian"),
  RUSSIAN("ru", "Russian"),
  SAMOAN("sm", "Samoan"),
  SANSKRIT("sa", "Sanskrit"),
  SCOTS_GAELIC("gd", "Scots Gaelic"),
  SEPEDI("nso", "Sepedi"),
  SERBIAN("sr", "Serbian"),
  SESOTHO("st", "Sesotho"),
  SHONA("sn", "Shona"),
  SINDHI("sd", "Sindhi"),
  SINHALA("si", "Sinhala"),
  SLOVAK("sk", "Slovak"),
  SLOVENIAN("sl", "Slovenian"),
  SOMALI("so", "Somali"),
  SPANISH("es", "Spanish"),
  SUNDANESE("su", "Sundanese"),
  SWAHILI("sw", "Swahili"),
  SWEDISH("sv", "Swedish"),
  TAGALOG_FILIPINO("tl", "Tagalog (Filipino)"),
  TAJIK("tg", "Tajik"),
  TAMIL("ta", "Tamil"),
  TATAR("tt", "Tatar"),
  TELUGU("te", "Telugu"),
  THAI("th", "Thai"),
  TIGRINYA("ti", "Tigrinya"),
  TSONGA("ts", "Tsonga"),
  TURKISH("tr", "Turkish"),
  TURKMEN("tk", "Turkmen"),
  TWI_AKAN("ak", "Twi_Akan"),
  UKRAINIAN("uk", "Ukrainian"),
  URDU("ur", "Urdu"),
  UYGHUR("ug", "Uyghur"),
  UZBEK("uz", "Uzbek"),
  VIETNAMESE("vi", "Vietnamese"),
  WELSH("cy", "Welsh"),
  XHOSA("xh", "Xhosa"),
  YIDDISH("yi", "Yiddish"),
  YORUBA("yo", "Yoruba"),
  ZULU("zu", "Zulu"),
  UK("en-GB", "UK"),
  US("en-US", "US"),
  AUTO_DETECT("auto", "Detect Language");

  private final String code;
  private final String name;

  Language(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public static String getCode(String name) {
    for (Language language : Language.values()) {
      if (name.equals(language.name)) {
        return language.code;
      }
    }
    return "";
  }

  public String getName() {
    return name;
  }

  public static String getName(String code) {
    for (Language language : Language.values()) {
      if (code.equals(language.code)) {
        return language.name;
      }
    }
    return "";
  }

  public static Language getLanguage(String code_or_name) {
    for (Language language : Language.values()) {
      if (language.getCode().equals(code_or_name) ||
          language.getName().equals(code_or_name)) {
        return language;
      }
    }
    return AUTO_DETECT;
  }

}
