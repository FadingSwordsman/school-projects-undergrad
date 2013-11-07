import Network (listenOn, withSocketsDo, accept, PortID(..), Socket)
import System.Environment (getArgs)
import System.IO (hSetBuffering, hGetLine, hPutStrLn, hSetEcho, BufferMode(..), Handle)
import Control.Concurrent (forkIO)

main :: IO ()
main = do 
	args <- getArgs
	let port = fromIntegral ( read $ head args :: Int )
	sock <- listenOn $ PortNumber port
	putStrLn $ "Listening on port " ++ (head args)
	sockHandler sock

sockHandler :: Socket -> IO ()
sockHandler sock = do
	(handle, _, _) <- accept sock
	putStrLn $ "Accepted connection"
	hSetBuffering handle NoBuffering
	forkIO $ commandProcessor handle
	sockHandler sock

commandProcessor :: Handle -> IO ()
commandProcessor handle = do
	line <- hGetLine handle
	echo handle line
	commandProcessor handle

echo :: Handle -> String -> IO ()
echo handle text = do
	putStrLn text
	hPutStrLn handle text
