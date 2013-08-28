import os, os.path, sys
import urllib, zipfile
import shutil, glob, fnmatch
import subprocess, logging
import shutil
from optparse import OptionParser
    
def soulforge_main(soulforge_dir, mcp_dir):
    sys.path.append(mcp_dir)
    from runtime.reobfuscate import reobfuscate
    from soulforge import copytree, reset_logger, recompile, copyreobfuscatedfiles, unzipandcopybtw, movetodist, packagedist, cleanup
    from update_binary_patches import updatepatches
    
    print '=============================== Build Soulforge Start ====================================='

    print 'Copying Soulforge Files'
    copytree(os.path.join(soulforge_dir, 'src', 'minecraft'), os.path.join(mcp_dir, 'src', 'minecraft')) 
    copytree(os.path.join(soulforge_dir, 'src', 'common'), os.path.join(mcp_dir, 'src', 'minecraft')) 
    copytree(os.path.join(soulforge_dir, 'src', 'minecraft_server'), os.path.join(mcp_dir, 'src', 'minecraft_server')) 
    copytree(os.path.join(soulforge_dir, 'src', 'common'), os.path.join(mcp_dir, 'src', 'minecraft_server'))
    copytree(os.path.join(soulforge_dir, 'srcbiomesoplenty', 'minecraft'), os.path.join(mcp_dir, 'src', 'minecraft'))
    copytree(os.path.join(soulforge_dir, 'srcbiomesoplenty', 'common'), os.path.join(mcp_dir, 'src', 'minecraft'))
    copytree(os.path.join(soulforge_dir, 'srcbiomesoplenty', 'minecraft_server'), os.path.join(mcp_dir, 'src', 'minecraft_server'))
    copytree(os.path.join(soulforge_dir, 'srcbiomesoplenty', 'common'), os.path.join(mcp_dir, 'src', 'minecraft_server'))
    copytree(os.path.join(soulforge_dir, 'srccustomores', 'minecraft'), os.path.join(mcp_dir, 'src', 'minecraft'))
    copytree(os.path.join(soulforge_dir, 'srccustomores', 'common'), os.path.join(mcp_dir, 'src', 'minecraft'))
    copytree(os.path.join(soulforge_dir, 'srccustomores', 'minecraft_server'), os.path.join(mcp_dir, 'src', 'minecraft_server'))
    copytree(os.path.join(soulforge_dir, 'srccustomores', 'common'), os.path.join(mcp_dir, 'src', 'minecraft_server'))

    os.chdir(mcp_dir)
    reset_logger()
    print 'Recompiling'
    recompile(None, False, False)
    reset_logger()
    print 'Reobfuscating'
    reobfuscate(None, False, False, False, False, False, False)
    reset_logger()
    print 'Copying Reobfuscated Files'
    copyreobfuscatedfiles(soulforge_dir, mcp_dir)
    print 'Unzipping BTW & Copying Files'
    unzipandcopybtw(soulforge_dir)
    print 'Creating Binary Patches'
    updatepatches()
    print 'Moving Files To Temporary Dist Folder'
    movetodist(soulforge_dir)
    print 'Packaging Files'
    packagedist(soulforge_dir)
    print 'Cleaning Up'
    cleanup(soulforge_dir, mcp_dir)

    print '=============================== Build Soulforge Finished ================================='

if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option('-m', '--mcp-dir',   action='store',      dest='mcp_dir',       help='Path to download/extract MCP to',         default=None )
    options, _ = parser.parse_args()
    
    soulforge_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    mcp_dir = os.path.abspath('mcp')

    if not options.mcp_dir is None:
        mcp_dir = os.path.abspath(options.mcp_dir)
    
    soulforge_main(soulforge_dir, mcp_dir)