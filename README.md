# classroom-games-client


```classDiagram
    %% =======================
    %% Core Entities
    %% =======================
    class PlayerData {
        +name
        +score
        +status
    }

    %% =======================
    %% Games (Pure Rules / Logic)
    %% =======================
    class Game {
        <<abstract>>
        +start()
        +end()
        +isValidMove()
        +checkWinCondition()
        +getBoardState()
    }

    class TicTacToeGame
    class ConnectFourGame
    class SuperTicTacToeGame

    Game <|-- TicTacToeGame
    Game <|-- ConnectFourGame
    Game <|-- SuperTicTacToeGame

    %% =======================
    %% Game Context (Wrapper / Session)
    %% =======================
    class GameContext {
        <<abstract>>
        +initializeGame()
    }

    class OnlineGameContext {
        +connectServer()
    }

    class OfflineSingleGameContext
    class OfflineMultipleGameContext

    GameContext <|-- OnlineGameContext
    GameContext <|-- OfflineSingleGameContext
    GameContext <|-- OfflineMultipleGameContext

    %% =======================
    %% AI Strategy
    %% =======================
    class AiStrategy {
        <<interface>>
        +makeMove()
        +easyMove()
        +mediumMove()
        +hardMove()
    }

    class TicTacToeAI
    class ConnectFourAI
    class SuperTicTacToeAI

    AiStrategy <|-- TicTacToeAI
    AiStrategy <|-- ConnectFourAI
    AiStrategy <|-- SuperTicTacToeAI

    %% =======================
    %% Managers
    %% =======================
    class RecordManager {
        +saveRecord()
        +loadRecord()
    }

    class PrizeManager {
        +awardPrize()
    }

    %% =======================
    %% Final Relationships
    %% =======================
    GameContext *-- Game             : wraps
    GameContext o-- PlayerData       : players
    OfflineSingleGameContext --> AiStrategy    : uses
    GameContext --> RecordManager
    GameContext --> PrizeManager
    ```
