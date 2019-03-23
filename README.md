![license](https://img.shields.io/badge/license-MIT_2.0-blue.svg) ![license](https://img.shields.io/badge/stable-0.1.0-brightgreen.svg) ![minSdk](https://img.shields.io/badge/minSdk-Lollipop-orange.svg)

# Kirinmaru

Kirinmaru is a FOSS Android app for reading translated web novels (primarily Chinese and Korean).

Feel free to report issues here on Github.

<img src="/graphics/p_nav_side.png" width="256" /><img src="/graphics/p_lib.png" width="256" /><img src="/graphics/p_cat_2.png" width="256" />
<img src="/graphics/p_chap.png" width="256" /><img src="/graphics/p_read_config.png" width="256" /><img src="/graphics/p_read.png" width="256" />

### Supported sites

Current site plugins:
- Wuxiaworld

Planned:
- VolareNovels
- Other Wordpress-based sites
- Qidian?

## Contributing

Plugin implementations are under `kirinmaru-core` and must be registered under [PluginModule](stream/reconfig/kirinmaru/plugins/PluginModule.kt)

Fork, branch, squash and rebase with proper commit messages before submitting your pull request.

Please follow Google code style before submitting. For Kotlin, the below settings are enough in IntelliJ IDEA/Android Studio:
- `2` for tab size and indent
- `4` for continuation indent

## License

This project is provided under [MIT 2.0](LICENSE.md)
