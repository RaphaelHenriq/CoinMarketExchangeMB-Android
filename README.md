# 📱 CoinMarketExchangeMB (Android)

Aplicativo Android focado no monitoramento de exchanges de criptomoedas, fornecendo detalhes técnicos e listagem de ativos em tempo real. 
Este projeto foi desenvolvido para demonstrar proficiência em tecnologias modernas de desenvolvimento nativo, padrões de arquitetura e boas práticas de UI/UX.


| Gif | Primeira tela | Segunda tela |
| --- |  --- |  --- | 
| <img src="https://github.com/user-attachments/assets/548c12b7-4832-4a9f-acad-769a1ea5fe2a" width="300" /> | <img width="1080" height="2400" alt="Screenshot_20260305_144855" src="https://github.com/user-attachments/assets/89b4a895-a6f5-4191-b798-a8b12349e1c5" /> | <img width="1080" height="2400" alt="Screenshot_20260305_144824" src="https://github.com/user-attachments/assets/9a9eacb5-37e3-4bda-a86d-c0bb230f697c" /> |


## 🛠 Explicação técnica

O projeto segue os princípios da Clean Architecture e é estruturado por camadas dentro de cada feature (Package by Feature) para facilitar a escalabilidade.

- di/: Configurações de Injeção de Dependência (Hilt/Koin).
- network/: Configuração central do Retrofit, interceptors para API Keys e Logs.
- utils/: Helpers para formatação de moeda, datas e tratamento de strings.

**📂 Features:** pasta com as features do projeto, independentes, como se fossem módulos

ExchangesList e ExchangeDetails: features de tela, cada uma com sua implementação diferente, explicado logo abaixo
- presentation/: ViewModels e UI em Jetpack Compose.
- data/: Implementação de Repositórios e Data Sources (Retrofit Services).

### Tecnologias e padrões
- Arquitetura: MVVM (Model-View-ViewModel) com Unidirectional Data Flow (UDF).
- UI: Jetpack Compose com design system baseado em Material 3.
- Gerenciamento de Estado: Sealed Classes para representar estados de UI (Loading, Success, Error).
- Asincronismo: Kotlin Coroutines e Flow para chamadas de rede e reatividade.
- Network: Retrofit 2 com Kotlinx Serialization para o parsing de JSON.
- Imagens: Coil para carregamento assíncrono e cache de logos.

### 🧪 Testes unitário

- Testes Unitários: Implementados com JUnit 5 e MockK para garantir a lógica de Network, ViewModels e Repositórios.


## 🔮 Próximos passos

- Modularização: Divisão do projeto em módulos Gradle (:core, :network, :features).
- Offline Mode: Cache local utilizando Room Database.
- CI/CD: Configuração de GitHub Actions para rodar Linter (Ktlint) e testes automaticamente.


## ⚙️ Configurações do Ambiente e Setup

Para compilar e rodar este projeto, certifique-se de que seu ambiente atende aos seguintes requisitos:
- Android Studio: Jellyfish | 2023.3.1 ou superior.
- JDK / JVM: Java 17 (Recomendado para compatibilidade com o Gradle 8+).
- Gradle: 8.4
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Linguagem: Kotlin 1.9.20+

## Considerações

- Este projeto usou a chave de API pública do ambiente de SandBox da CoinMarketCap, ou seja, todos os dados estão mockados.
