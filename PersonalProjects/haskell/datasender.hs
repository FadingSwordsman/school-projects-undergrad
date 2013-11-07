import System.Environment (getArgs)
import Network
import System.IO (hGetLine, hPutStrLn, hClose, stdin, stdout, hIsEOF, Handle)
import System.CPUTime
import Text.Printf

main :: IO ()
main = do
	args <- getArgs
	printall args
	let hostname = head args
	let port = fromIntegral (read $ args !! 1 :: Integer)
	putStrLn $ "Beginning timing"
	start <- getCPUTime
	sock <- connectTo hostname $ PortNumber port
	putStrLn $ "Connection established"
	sendInput stdin sock
	end <- getCPUTime
	hClose sock
	let diff = (fromIntegral (end - start)) / (10^9)
	printf "Round trip: %.2f ms\n" (diff :: Double)

sendInput :: Handle -> Handle -> IO ()
sendInput input output = do
	end <- hIsEOF input
	if end then
		return ()
	else do
		line <- hGetLine input
		hPutStrLn output line
		sendInput input output

printall :: [String] -> IO ()
printall [] = do
	return ()
printall (x:xs) = do
	putStrLn $ x
	printall xs
